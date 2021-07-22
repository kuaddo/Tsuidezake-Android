package jp.kuaddo.tsuidezake.data.remote.internal

import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import jp.kuaddo.tsuidezake.core.runCatchingS
import jp.kuaddo.tsuidezake.data.repository.ApiResponse
import jp.kuaddo.tsuidezake.data.repository.ErrorResponse
import jp.kuaddo.tsuidezake.data.repository.SuccessResponse
import timber.log.Timber

internal suspend fun <T : Any, R> ApolloQueryCall<T>.toApiResponse(
    transform: suspend (T) -> R
): ApiResponse<R> = runCatchingS {
    when (val res = await().toApiResponse()) {
        is SuccessResponse -> SuccessResponse(transform(res.data))
        is ErrorResponse -> res
    }
}
    .fold(
        onSuccess = { it },
        onFailure = {
            Timber.e(it)
            ErrorResponse(it.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    )

internal suspend fun <T : Any, R : Any> ApolloMutationCall<T>.toApiResponse(
    transform: suspend (T) -> R
): ApiResponse<R> = runCatchingS {
    when (val res = await().toApiResponse()) {
        is SuccessResponse -> SuccessResponse(transform(res.data))
        is ErrorResponse -> res
    }
}
    .fold(
        onSuccess = { it },
        onFailure = {
            Timber.e(it)
            ErrorResponse(it.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    )

private fun <T : Any> Response<T>.toApiResponse(): ApiResponse<T> {
    if (data != null) return SuccessResponse(data!!)

    val message = errors?.let {
        it.forEach { error -> Timber.e(error.toString()) }
        it.joinToString(separator = "\n") { error -> error.message }
    } ?: UNKNOWN_ERROR_MESSAGE
    return ErrorResponse(message)
}

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
