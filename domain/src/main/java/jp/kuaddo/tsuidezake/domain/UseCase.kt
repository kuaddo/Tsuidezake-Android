package jp.kuaddo.tsuidezake.domain

import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher? = null
) {
    suspend operator fun invoke(parameter: P): Resource<R> {
        return runCatching {
            if (coroutineDispatcher == null) execute(parameter)
            else withContext(coroutineDispatcher) { execute(parameter) }
        }
            .onFailure { if (it is CancellationException) throw it }
            .fold(
                onSuccess = { it },
                onFailure = {
                    Timber.e(it)
                    ErrorResource(it.message ?: UNKNOWN_ERROR_MESSAGE, null)
                }
            )
    }

    protected abstract suspend fun execute(parameter: P): Resource<R>

    companion object {
        private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
    }
}

suspend operator fun <R> UseCase<Unit, R>.invoke(): Resource<R> = this(Unit)
