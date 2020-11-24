package jp.kuaddo.tsuidezake.data.repository.internal

import jp.kuaddo.tsuidezake.data.repository.ApiResponse
import jp.kuaddo.tsuidezake.data.repository.AuthService
import jp.kuaddo.tsuidezake.data.repository.ErrorResponse
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage
import jp.kuaddo.tsuidezake.data.repository.SuccessResponse
import jp.kuaddo.tsuidezake.data.repository.TsuidezakeService
import jp.kuaddo.tsuidezake.domain.Repository
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val localDataSource: LocalDataSource,
    private val authService: AuthService,
    private val tsuidezakeService: TsuidezakeService
) : Repository {
    override val isAccountInitialized: Flow<Boolean> = authService.initialized

    override suspend fun signInAnonymously(): Boolean = authService.signInAnonymously()

    override suspend fun getRankings(): Resource<List<Ranking>> =
        tsuidezakeService.getRankings().convertToResource()

    override suspend fun getRecommendedSakes(): Resource<List<Ranking.Content>> =
        tsuidezakeService.getRecommendedSakes().convertToResource()

    override suspend fun getWishList(): Resource<List<SakeDetail>> =
        tsuidezakeService.getWishList().convertToResource()

    override suspend fun getSakeDetail(id: Int): Resource<SakeDetail> =
        tsuidezakeService.getSakeDetail(id).convertToResource()

    override fun getUserSake(
        id: Int
    ): Flow<Resource<UserSake>> = object : NetworkBoundResource<UserSake, UserSake>() {
        override fun loadFromDb(): Flow<UserSake?> = localDataSource.loadUserSakeFlow(id)

        // TODO: キャッシュの生存期間を考える
        override fun shouldFetch(data: UserSake?): Boolean = true

        override suspend fun callApi(): ApiResponse<UserSake> = tsuidezakeService.getUserSake(id)

        override suspend fun saveApiResult(item: UserSake) = localDataSource.saveUserSake(item)
    }.getResultFlow()

    override suspend fun addSakeToWishList(id: Int): Resource<List<SakeDetail>> =
        tsuidezakeService.addSakeToWishList(id).convertToResource()

    override suspend fun removeSakeFromWishList(id: Int): Resource<List<SakeDetail>> =
        tsuidezakeService.removeSakeFromWishList(id).convertToResource()

    override suspend fun addSakeToTastedList(id: Int): Resource<List<SakeDetail>> =
        tsuidezakeService.addSakeToTastedList(id).convertToResource()

    override suspend fun removeSakeFromTastedList(id: Int): Resource<List<SakeDetail>> =
        tsuidezakeService.removeSakeFromTastedList(id).convertToResource()

    private fun <T : Any> ApiResponse<T>.convertToResource(): Resource<T> =
        when (this) {
            is SuccessResponse -> SuccessResource(data)
            is ErrorResponse -> ErrorResource(message, null)
        }
}
