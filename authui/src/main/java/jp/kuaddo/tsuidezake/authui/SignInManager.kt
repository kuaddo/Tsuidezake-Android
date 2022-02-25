package jp.kuaddo.tsuidezake.authui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.actionCodeSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.kuaddo.tsuidezake.core.ActivityRegistrationKeyCreator
import jp.kuaddo.tsuidezake.core.letS
import jp.kuaddo.tsuidezake.core.runCatchingS
import kotlin.reflect.KClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class SignInManager @AssistedInject constructor(
    @Assisted lifecycleOwner: LifecycleOwner,
    @Assisted activityResultRegistry: ActivityResultRegistry,
    @ApplicationContext private val context: Context,
    keyCreator: ActivityRegistrationKeyCreator
    // TODO: emailのハンドリングするクラスを切り出し、signInManagerから処理を委譲する
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val lifecycleScope = lifecycleOwner.lifecycleScope

    private val _isLinkSuccessfulEvent = MutableSharedFlow<Boolean>()
    val isLinkSuccessfulEvent: Flow<Boolean> = _isLinkSuccessfulEvent

    private val _isEmailSendingSuccessfulEvent = MutableSharedFlow<Boolean>()
    val isEmailSendingSuccessfulEvent: Flow<Boolean> = _isEmailSendingSuccessfulEvent

    private val googleSignInLauncher = activityResultRegistry.register(
        keyCreator.generateKey(),
        lifecycleOwner,
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result == null || result.resultCode != Activity.RESULT_OK) return@register

        lifecycleScope.launch {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .runCatchingS { await() }
                .getOrNull()
            if (account == null) {
                _isLinkSuccessfulEvent.emit(false)
                return@launch
            }

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseLinkWithCredential(credential)
        }
    }

    private val facebookCallbackManager = CallbackManager.Factory.create()
    private val facebookLoginManager = LoginManager.getInstance()

    init {
        lifecycleScope.launch {
            facebookLoginManager.getTokenFlow(facebookCallbackManager).collect { token ->
                if (token == null) {
                    _isLinkSuccessfulEvent.emit(false)
                    return@collect
                }

                val credential = FacebookAuthProvider.getCredential(token)
                firebaseLinkWithCredential(credential)
            }
        }
        lifecycleScope.launch {
            val pendingRequest = auth.pendingAuthResult ?: return@launch
            pendingRequest.runCatchingS { await() }
                .isSuccess
                .letS(_isLinkSuccessfulEvent::emit)
        }
    }

    private suspend fun firebaseLinkWithCredential(credential: AuthCredential) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _isLinkSuccessfulEvent.emit(false)
            return
        }

        currentUser.linkWithCredential(credential)
            .runCatchingS { await() }
            .fold(
                onSuccess = { it.user != null },
                onFailure = { false }
            )
            .letS(_isLinkSuccessfulEvent::emit)
    }

    fun linkByGoogle() {
        val signInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SIGN_IN_TOKEN)
            .build()
        val signInClient = GoogleSignIn.getClient(context, signInOption)
        googleSignInLauncher.launch(signInClient.signInIntent)
    }

    fun linkByTwitter(activity: Activity) = lifecycleScope.launch {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _isLinkSuccessfulEvent.emit(false)
            return@launch
        }

        val provider = OAuthProvider.newBuilder("twitter.com").build()
        currentUser.startActivityForLinkWithProvider(activity, provider)
            .runCatchingS { await() }
            .isSuccess
            .letS(_isLinkSuccessfulEvent::emit)
    }

    fun linkByFacebook(fragment: Fragment) {
        facebookLoginManager.logInWithReadPermissions(
            fragment,
            facebookCallbackManager,
            mutableListOf("email", "public_profile")
        )
    }

    // TODO: Emailの処理は完成していない
    fun linkByEmail(email: String) {
        val actionCodeSettings = actionCodeSettings {
            url = "https://tsuidezake.page.link/email_sign_in"
            handleCodeInApp = true
            setAndroidPackageName(
                context.packageName,
                true,
                null
            )
        }
        lifecycleScope.launch {
            // TODO: できれば毎回sign inは避けたい。事前にemailでログインしてみれば行ける？
            auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .runCatchingS { await() }
                .isSuccess
                .letS(_isEmailSendingSuccessfulEvent::emit)
        }
    }

    fun handleEmailIntent(intent: Intent, nextScreenClass: KClass<out Activity>) {
        val data = intent.data
        if (data == null) {
            Timber.d("Data inside email intent is null.")
            return
        }
        val link = data.toString()
        if (!auth.isSignInWithEmailLink(link)) {
            Timber.d("Email link was failed.")
            return
        }

        lifecycleScope.launch {
            // TODO: shared prefからemailを取得。暗号化してあげる
            auth.signInWithEmailLink("shiita0903@gmail.com", link)
                .runCatchingS { await() }
                .fold(
                    onSuccess = { it.user != null },
                    onFailure = { false }
                )
                .letS(_isLinkSuccessfulEvent::emit)

            // TODO: activityのaffinityを考える。別タスクにならないように
            context.startActivity(Intent(context, nextScreenClass.java))
        }
    }

    companion object {
        @OptIn(ExperimentalCoroutinesApi::class)
        private fun LoginManager.getTokenFlow(
            callbackManager: CallbackManager
        ): Flow<String?> = callbackFlow {
            registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        trySend(result.accessToken.token)
                    }

                    override fun onCancel() = Unit

                    override fun onError(error: FacebookException) {
                        trySend(null)
                    }
                }
            )

            awaitClose { unregisterCallback(callbackManager) }
        }
    }
}
