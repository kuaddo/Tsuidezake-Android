package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWishListUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Unit, Resource<List<SakeDetail>>>() {
    override fun execute(parameter: Unit): Flow<Resource<List<SakeDetail>>> =
        repository.getWishList().map { result ->
            result.map { wishList -> wishList.sortedBy(SakeDetail::id) }
        }
}
