package jp.kuaddo.tsuidezake.model

sealed class Resource<out T> {
    fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is SuccessResource -> SuccessResource(transform(data))
        is ErrorResource -> ErrorResource(message, data?.let { transform(it) })
        is LoadingResource -> LoadingResource
    }

    fun <R> flatMap(transform: (T) -> Resource<R>): Resource<R> = when (this) {
        is SuccessResource -> transform(data)
        is ErrorResource -> {
            when (val result = data?.let(transform)) {
                is SuccessResource -> ErrorResource(message, result.data)
                is ErrorResource -> ErrorResource("$message\n${result.message}", result.data)
                LoadingResource -> LoadingResource
                null -> ErrorResource(message, null)
            }
        }
        LoadingResource -> LoadingResource
    }

    fun dataOrNull(): T? = when (this) {
        is SuccessResource -> data
        is ErrorResource -> data
        LoadingResource -> null
    }
}

data class SuccessResource<out T>(val data: T) : Resource<T>()

data class ErrorResource<out T>(val message: String, val data: T?) : Resource<T>()

object LoadingResource : Resource<Nothing>()