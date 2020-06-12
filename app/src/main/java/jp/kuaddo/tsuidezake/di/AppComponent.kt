package jp.kuaddo.tsuidezake.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import jp.kuaddo.tsuidezake.TsuidezakeApp
import jp.kuaddo.tsuidezake.data.repository.RepositoryModule
import jp.kuaddo.tsuidezake.di.module.ActivityModule
import jp.kuaddo.tsuidezake.di.module.AppModule
import jp.kuaddo.tsuidezake.di.module.ViewModelDelegateModule
import jp.kuaddo.tsuidezake.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        ViewModelDelegateModule::class,
        AppModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent : AndroidInjector<TsuidezakeApp> {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: TsuidezakeApp,
            @BindsInstance applicationContext: Context
        ): AppComponent
    }

    override fun inject(app: TsuidezakeApp)
}
