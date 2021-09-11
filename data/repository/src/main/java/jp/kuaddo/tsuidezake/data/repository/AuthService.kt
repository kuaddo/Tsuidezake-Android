package jp.kuaddo.tsuidezake.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthService {
    val initialized: Flow<Boolean>

    suspend fun signInAnonymously(): Boolean
    fun signOut()
}
