package jp.kuaddo.tsuidezake

import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jp.kuaddo.tsuidezake.data.local.DaggerLocalDataComponent
import jp.kuaddo.tsuidezake.data.repository.DaggerRepositoryComponent
import jp.kuaddo.tsuidezake.di.DaggerAppComponent
import timber.log.Timber

class TsuidezakeApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        // TODO: Componentの生成は各自でやりたい
        DaggerAppComponent.factory()
            .create(
                this,
                applicationContext,
                DaggerRepositoryComponent.factory().create(
                    applicationContext,
                    DaggerLocalDataComponent.factory().create(applicationContext)
                )
            )
}
