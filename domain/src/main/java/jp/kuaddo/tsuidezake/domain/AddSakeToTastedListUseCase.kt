package jp.kuaddo.tsuidezake.domain

import androidx.annotation.IntRange
import jp.kuaddo.tsuidezake.model.Resource
import javax.inject.Inject

class AddSakeToTastedListUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<AddSakeToTastedListUseCase.Parameter, Unit>() {
    override suspend fun execute(parameter: Parameter): Resource<Unit> =
        repository.addSakeToTastedList(parameter.sakeId, parameter.evaluation)

    data class Parameter(
        val sakeId: Int,
        @IntRange(from = 1, to = 5) val evaluation: Int
    )
}
