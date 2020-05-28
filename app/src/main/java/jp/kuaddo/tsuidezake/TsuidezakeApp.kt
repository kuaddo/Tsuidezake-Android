package jp.kuaddo.tsuidezake

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jp.kuaddo.tsuidezake.data.PreferenceStorage
import jp.kuaddo.tsuidezake.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class TsuidezakeApp : DaggerApplication() {
    @Inject
    lateinit var preferenceStorage: PreferenceStorage


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        preferenceStorage.preferenceChangedEvent.call() // 呼んでおくことで、mapした直後に値を反映できる
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .applicationContext(this.applicationContext)
            .build()
    }
}