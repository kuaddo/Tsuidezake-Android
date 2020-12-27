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

    override fun getRankings(): Flow<Resource<List<Ranking>>> =
        object : NetworkBoundResource<List<Ranking>, List<Ranking>>() {
            override fun loadFromDb(): Flow<List<Ranking>?> =
                localDataSource.loadRankingsFlow()

            // TODO: キャッシュの生存期間を考える
            override fun shouldFetch(data: List<Ranking>?): Boolean = true

            override suspend fun callApi(): ApiResponse<List<Ranking>> =
                tsuidezakeService.getRankings()

            override suspend fun saveApiResult(item: List<Ranking>) =
                localDataSource.saveRankings(item)
        }.getResultFlow()

    override fun getRecommendedSakes(): Flow<Resource<List<Ranking.Content>>> =
        object : NetworkBoundResource<List<Ranking.Content>, List<Ranking.Content>>() {
            override fun loadFromDb(): Flow<List<Ranking.Content>?> =
                localDataSource.loadRecommendedSakesFlow()

            // TODO: キャッシュの生存期間を考える
            override fun shouldFetch(data: List<Ranking.Content>?): Boolean = true

            override suspend fun callApi(): ApiResponse<List<Ranking.Content>> =
                tsuidezakeService.getRecommendedSakes()

            override suspend fun saveApiResult(item: List<Ranking.Content>) =
                localDataSource.saveRecommendedSakes(item)
        }.getResultFlow()

    override fun getWishList(): Flow<Resource<List<SakeDetail>>> =
        object : NetworkBoundResource<List<SakeDetail>, List<SakeDetail>>() {
            override fun loadFromDb(): Flow<List<SakeDetail>?> = localDataSource.loadWishListFlow()

            // TODO: キャッシュの生存期間を考える
            override fun shouldFetch(data: List<SakeDetail>?): Boolean = true

            override suspend fun callApi(): ApiResponse<List<SakeDetail>> =
                tsuidezakeService.getWishList()

            override suspend fun saveApiResult(item: List<SakeDetail>) =
                localDataSource.saveWishList(item)
        }.getResultFlow()

    override fun getSakeDetail(
        id: Int
    ): Flow<Resource<SakeDetail>> = object : NetworkBoundResource<SakeDetail, SakeDetail>() {
        override fun loadFromDb(): Flow<SakeDetail?> = localDataSource.loadSakeDetailFlow(id)

        // TODO: キャッシュの生存期間を考える
        override fun shouldFetch(data: SakeDetail?): Boolean = true

        override suspend fun callApi(): ApiResponse<SakeDetail> =
            tsuidezakeService.getSakeDetail(id)

        override suspend fun saveApiResult(item: SakeDetail) = localDataSource.saveSakeDetail(item)
    }.getResultFlow()

    override fun getUserSake(
        id: Int
    ): Flow<Resource<UserSake>> = object : NetworkBoundResource<UserSake, UserSake>() {
        override fun loadFromDb(): Flow<UserSake?> = localDataSource.loadUserSakeFlow(id)

        // TODO: キャッシュの生存期間を考える
        override fun shouldFetch(data: UserSake?): Boolean = true

        override suspend fun callApi(): ApiResponse<UserSake> = tsuidezakeService.getUserSake(id)

        override suspend fun saveApiResult(item: UserSake) = localDataSource.saveUserSake(item)
    }.getResultFlow()

    // TODO: レスポンスが修正され次第NetworkBoundResource対応をする
    override suspend fun addSakeToWishList(id: Int): Resource<UserSake> =
        tsuidezakeService.addSakeToWishList(id).convertToResource()

    override suspend fun removeSakeFromWishList(id: Int): Resource<UserSake> =
        tsuidezakeService.removeSakeFromWishList(id).convertToResource()

    override suspend fun addSakeToTastedList(id: Int): Resource<UserSake> =
        tsuidezakeService.addSakeToTastedList(id).convertToResource()

    override suspend fun removeSakeFromTastedList(id: Int): Resource<UserSake> =
        tsuidezakeService.removeSakeFromTastedList(id).convertToResource()

    private fun <T : Any> ApiResponse<T>.convertToResource(): Resource<T> =
        when (this) {
            is SuccessResponse -> SuccessResource(data)
            is ErrorResponse -> ErrorResource(message, null)
        }
}
