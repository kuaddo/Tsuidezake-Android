package jp.kuaddo.tsuidezake.data.auth

import androidx.lifecycle.LiveData

interface AuthService {
    val token: String?
    val initialized: LiveData<Boolean>

    fun startListening()
}
