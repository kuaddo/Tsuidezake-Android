package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.model.Resource
import javax.inject.Inject

class RemoveSakeFromTastedListUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Int, Unit>() {
    override suspend fun execute(parameter: Int): Resource<Unit> =
        repository.removeSakeFromTastedList(parameter).ignoreData()
}
