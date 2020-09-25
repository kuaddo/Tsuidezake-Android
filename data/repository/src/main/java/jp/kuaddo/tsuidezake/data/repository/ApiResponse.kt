package jp.kuaddo.tsuidezake.data.repository

sealed class ApiResponse<out T : Any>

data class SuccessResponse<T : Any>(val data: T) : ApiResponse<T>()

data class ErrorResponse(val message: String) : ApiResponse<Nothing>()
