package jp.kuaddo.tsuidezake

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import jp.kuaddo.tsuidezake.domain.SignInAnonymouslyUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TsuidezakeApp : Application() {
    @Inject
    lateinit var signInAnonymouslyUseCase: SignInAnonymouslyUseCase

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        registerAuthService()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun registerAuthService() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        getSystemService<ConnectivityManager>()?.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    GlobalScope.launch { signInAnonymouslyUseCase() }
                }
            }
        )
    }
}
