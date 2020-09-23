package jp.kuaddo.tsuidezake.data.auth

import androidx.lifecycle.LiveData

interface AuthService {
    val initialized: LiveData<Boolean>

    fun signInAnonymously()
}
