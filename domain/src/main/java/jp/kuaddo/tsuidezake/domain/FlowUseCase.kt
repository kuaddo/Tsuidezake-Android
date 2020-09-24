package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher? = null
) {
    operator fun invoke(parameter: P): Flow<Resource<R>> = execute(parameter)
        .catch { throwable ->
            Timber.e(throwable)
            emit(ErrorResource(throwable.message ?: UNKNOWN_ERROR_MESSAGE, null))
        }
        .let { flow ->
            if (coroutineDispatcher != null) flow.flowOn(coroutineDispatcher) else flow
        }

    protected abstract fun execute(parameter: P): Flow<Resource<R>>

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
    }
}

operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<Resource<R>> = this(Unit)
