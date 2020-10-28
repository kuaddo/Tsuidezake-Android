package jp.kuaddo.tsuidezake.data.remote.internal

import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import jp.kuaddo.tsuidezake.data.repository.ApiResponse
import jp.kuaddo.tsuidezake.data.repository.ErrorResponse
import jp.kuaddo.tsuidezake.data.repository.SuccessResponse
import kotlinx.coroutines.CancellationException
import timber.log.Timber

internal fun <T : Any> Response<T>.toApiResponse(): ApiResponse<T> {
    if (data != null) return SuccessResponse(data!!)

    val message = errors?.let {
        it.map { error -> Timber.d(error.message) }
        it.joinToString(separator = "\n") { error -> error.message }
    } ?: UNKNOWN_ERROR_MESSAGE
    return ErrorResponse(message)
}

internal suspend fun <T : Any, R : Any> ApolloQueryCall<T>.toApiResponse(
    transform: suspend (T) -> R
): ApiResponse<R> = runCatching {
    when (val res = await().toApiResponse()) {
        is SuccessResponse -> SuccessResponse(transform(res.data))
        is ErrorResponse -> res
    }
}
    .onFailure { if (it is CancellationException) throw it }
    .fold(
        onSuccess = { it },
        onFailure = {
            Timber.e(it)
            ErrorResponse(it.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    )

internal suspend fun <T : Any, R : Any> ApolloMutationCall<T>.toApiResponse(
    transform: suspend (T) -> R
): ApiResponse<R> = runCatching {
    when (val res = await().toApiResponse()) {
        is SuccessResponse -> SuccessResponse(transform(res.data))
        is ErrorResponse -> res
    }
}
    .onFailure { if (it is CancellationException) throw it }
    .fold(
        onSuccess = { it },
        onFailure = {
            Timber.e(it)
            ErrorResponse(it.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    )

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
