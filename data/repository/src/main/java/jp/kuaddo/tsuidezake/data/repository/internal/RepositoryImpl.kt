package jp.kuaddo.tsuidezake.data.repository.internal

import jp.kuaddo.tsuidezake.data.local.PreferenceStorage
import jp.kuaddo.tsuidezake.data.remote.ApiResponse
import jp.kuaddo.tsuidezake.data.remote.ErrorResponse
import jp.kuaddo.tsuidezake.data.remote.SuccessResponse
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeService
import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val service: TsuidezakeService
) : Repository {
    override suspend fun getRankings(): Resource<List<Ranking>> =
        service.getRankings().convertToResource()

    override suspend fun getSakeDetail(id: Int): Resource<SakeDetail> =
        service.getSakeDetail(id).convertToResource()

    override suspend fun addSakeToWishList(id: Int): Resource<List<SakeDetail>> =
        service.addSakeToWishList(id).convertToResource()

    override suspend fun removeSakeFromWishList(id: Int): Resource<List<SakeDetail>> =
        service.removeSakeFromWishList(id).convertToResource()

    override suspend fun addSakeToTastedList(id: Int): Resource<List<SakeDetail>> =
        service.addSakeToTastedList(id).convertToResource()

    override suspend fun removeSakeFromTastedList(id: Int): Resource<List<SakeDetail>> =
        service.removeSakeFromTastedList(id).convertToResource()

    private fun <T : Any> ApiResponse<T>.convertToResource(): Resource<T> =
        when (this) {
            is SuccessResponse -> SuccessResource(data)
            is ErrorResponse -> ErrorResource(message, null)
        }
}
