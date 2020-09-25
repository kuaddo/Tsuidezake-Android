package jp.kuaddo.tsuidezake.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher? = null
) {
    operator fun invoke(parameter: P): Flow<R> = execute(parameter)
        .catch { throwable -> Timber.e(throwable) }
        .let { flow ->
            if (coroutineDispatcher != null) flow.flowOn(coroutineDispatcher) else flow
        }

    protected abstract fun execute(parameter: P): Flow<R>
}

operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<R> = this(Unit)
