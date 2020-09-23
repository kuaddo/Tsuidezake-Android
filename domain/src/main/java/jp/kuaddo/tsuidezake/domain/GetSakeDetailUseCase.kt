package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import javax.inject.Inject

class GetSakeDetailUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Int, SakeDetail>() {
    override suspend fun execute(parameter: Int): Resource<SakeDetail> =
        repository.getSakeDetail(parameter)
}
