package jp.kuaddo.tsuidezake

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class TsuidezakeApp : DaggerApplication() {
    @Inject
    lateinit var authService: AuthService

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        registerAuthService()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    private fun registerAuthService() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        getSystemService<ConnectivityManager>()?.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    authService.startListening()
                    authService.signInAnonymously()
                }
            }
        )
    }
}
