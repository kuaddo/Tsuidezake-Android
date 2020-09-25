package jp.kuaddo.tsuidezake.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsAccountInitializedUseCase @Inject constructor(
    private val repository: Repository
) : FlowUseCase<Unit, Boolean>() {
    override fun execute(parameter: Unit): Flow<Boolean> = repository.isAccountInitialized
}
