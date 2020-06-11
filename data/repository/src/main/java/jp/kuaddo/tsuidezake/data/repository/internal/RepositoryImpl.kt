package jp.kuaddo.tsuidezake.data.repository.internal

import jp.kuaddo.tsuidezake.data.local.PreferenceStorage
import jp.kuaddo.tsuidezake.data.remote.ErrorResponse
import jp.kuaddo.tsuidezake.data.remote.SuccessResponse
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeService
import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import javax.inject.Inject

internal class RepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val service: TsuidezakeService
) : Repository {
    override suspend fun getSakeDetail(id: Int): Resource<SakeDetail> =
        when (val res = service.getSakeDetail(id)) {
            is SuccessResponse -> SuccessResource(res.data)
            is ErrorResponse -> ErrorResource(res.message, null)
        }
}
