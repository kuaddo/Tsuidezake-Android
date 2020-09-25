package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.Resource
import javax.inject.Inject

class AddSakeToWishListUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Int, Unit>() {
    override suspend fun execute(parameter: Int): Resource<Unit> =
        repository.addSakeToWishList(parameter).ignoreData()
}
