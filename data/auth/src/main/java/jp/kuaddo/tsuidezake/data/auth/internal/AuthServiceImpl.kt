package jp.kuaddo.tsuidezake.data.auth.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AuthenticationScope
internal class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthService {
    override var token: String? = null
        private set(value) {
            field = value
            _initialized.postValue(true)
        }

    override val initialized: LiveData<Boolean>
        get() = _initialized
    private val _initialized = MutableLiveData(false)

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

    override fun startListening() {
        if (isAddedListener.getAndSet(true)) return
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun signInAnonymously() {
        if (signInJob?.isCompleted == false && firebaseAuth.currentUser != null) return

        signInJob = GlobalScope.launch {
            firebaseAuth.signInAnonymously().await()
        }
    }
}
