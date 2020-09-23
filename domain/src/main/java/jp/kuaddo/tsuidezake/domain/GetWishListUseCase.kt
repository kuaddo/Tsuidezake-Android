package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import jp.kuaddo.tsuidezake.model.SakeDetail
import javax.inject.Inject

class GetWishListUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Unit, List<SakeDetail>>() {
    override suspend fun execute(parameter: Unit): Resource<List<SakeDetail>> =
        repository.getWishList()
            .map { wishList -> wishList.sortedBy(SakeDetail::id) }
}
