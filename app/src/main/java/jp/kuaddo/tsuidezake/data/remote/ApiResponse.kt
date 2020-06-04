package jp.kuaddo.tsuidezake.data.remote

import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import timber.log.Timber

sealed class ApiResponse<out T : Any>

data class SuccessResponse<T : Any>(val data: T) : ApiResponse<T>()

data class ErrorResponse(val message: String) : ApiResponse<Nothing>()

fun <T : Any> Response<T>.toApiResponse(): ApiResponse<T> {
    if (data != null) return SuccessResponse(data!!)

    val message = errors?.let {
        it.map { error -> Timber.d(error.message) }
        it.joinToString(separator = "\n") { error -> error.message }
    } ?: UNKNOWN_ERROR_MESSAGE
    return ErrorResponse(message)
}

suspend fun <T : Any, R : Any> ApolloQueryCall<T>.toApiResponse(
    transform: (T) -> R
): ApiResponse<R> = runCatching {
    when (val res = toDeferred().await().toApiResponse()) {
        is SuccessResponse -> SuccessResponse(transform(res.data))
        is ErrorResponse -> res
    }
}.fold(
    onSuccess = { it },
    onFailure = {
        Timber.e(it)
        ErrorResponse(it.message ?: UNKNOWN_ERROR_MESSAGE)
    }
)

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
