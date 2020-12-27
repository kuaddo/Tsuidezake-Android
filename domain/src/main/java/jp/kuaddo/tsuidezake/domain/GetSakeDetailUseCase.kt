package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSakeDetailUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Int, Resource<SakeDetail>>() {
    override fun execute(parameter: Int): Flow<Resource<SakeDetail>> =
        repository.getSakeDetail(parameter)
}
