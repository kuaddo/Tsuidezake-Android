package jp.kuaddo.tsuidezake.data.auth

interface AuthService {
    val token: String?

    fun startListening()
}
