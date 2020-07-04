package jp.kuaddo.tsuidezake.data.auth.internal

import com.google.firebase.auth.FirebaseAuth
import jp.kuaddo.tsuidezake.data.auth.AuthService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthService {
    override val token: String?
        get() = _token
    private var _token: String? = null

    private val lock = ReentrantLock()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        Timber.d("Received a FirebaseAuth update.")

        GlobalScope.launch {
            val tokenResult = runCatching {
                auth.currentUser
                    ?.getIdToken(true)
                    ?.await()
            }
                .onFailure { if (it is CancellationException) throw it }
                .getOrNull()
                ?.token

            _token = tokenResult
            if (tokenResult != null) {
                Timber.d("getIdToken() is successful. token = $tokenResult")
            } else {
                Timber.d("getIdToken() is failed.")
            }
        }
    }

    override fun startListening() = lock.withLock {
        firebaseAuth.removeAuthStateListener(authStateListener)
        firebaseAuth.addAuthStateListener(authStateListener)
    }
}