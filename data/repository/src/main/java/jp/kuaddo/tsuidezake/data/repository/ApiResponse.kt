package jp.kuaddo.tsuidezake.data.repository

sealed class ApiResponse<out T>

data class SuccessResponse<T>(val data: T) : ApiResponse<T>()

data class ErrorResponse(val message: String) : ApiResponse<Nothing>()
