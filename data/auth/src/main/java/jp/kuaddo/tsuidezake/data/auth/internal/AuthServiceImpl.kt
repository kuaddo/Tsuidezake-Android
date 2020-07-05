package jp.kuaddo.tsuidezake.data.auth.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import jp.kuaddo.tsuidezake.data.auth.AuthService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import kotlin.concurrent.withLock

class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthService {
    override var token: String? = null
        private set(value) {
            field = value
            _initialized.postValue(true)
        }

    override val initialized: LiveData<Boolean>
        get() = _initialized
    private val _initialized = MutableLiveData<Boolean>(false)

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

            token = tokenResult
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