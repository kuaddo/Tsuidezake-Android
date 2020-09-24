package jp.kuaddo.tsuidezake.data.auth.internal

import com.google.firebase.auth.FirebaseAuth
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationScope
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
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
    private var signInJob: Job? = null

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

    override fun signInAnonymously() {
        if (!isAddedListener.getAndSet(true)) {
            firebaseAuth.addAuthStateListener(authStateListener)
        }
        if (firebaseAuth.currentUser == null && signInJob?.isCompleted != false) {
            signInJob = GlobalScope.launch {
                firebaseAuth.signInAnonymously().await()
            }
        }
    }
}
