package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import javax.inject.Inject

class RemoveSakeFromWishListUseCase @Inject constructor(
    private val repository: Repository
) : UseCaseS<Int, Unit>() {
    override suspend fun execute(parameter: Int): Resource<Unit> =
        repository.removeSakeFromWishList(parameter)
}
