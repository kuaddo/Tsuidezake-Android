package jp.kuaddo.tsuidezake.data.auth.internal

import com.google.firebase.auth.FirebaseAuth
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationScope
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AuthenticationScope
internal class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthToken, AuthService {
    override var token: String? = null
        private set(value) {
            field = value
            _initialized.value = true
        }

    private val _initialized = MutableStateFlow(false)
    override val initialized: Flow<Boolean> = _initialized

    private val isAddedListener = AtomicBoolean(false)
    private val signInMutex = Mutex()

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

            token = tokenResult
            if (tokenResult != null) {
                Timber.d("getIdToken() is successful. token = $tokenResult")
            } else {
                Timber.d("getIdToken() is failed.")
            }
        }
    }

    override suspend fun signInAnonymously(): Boolean = signInMutex.withLock {
        if (!isAddedListener.getAndSet(true)) {
            firebaseAuth.addAuthStateListener(authStateListener)
        }
        if (firebaseAuth.currentUser == null) {
            val result = firebaseAuth.signInAnonymously().await()
            result.user != null
        } else {
            true
        }
    }
}
