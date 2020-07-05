package jp.kuaddo.tsuidezake.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import jp.kuaddo.tsuidezake.TsuidezakeApp
import jp.kuaddo.tsuidezake.data.auth.AuthenticationComponent
import jp.kuaddo.tsuidezake.data.auth.DaggerAuthenticationComponent
import jp.kuaddo.tsuidezake.data.repository.DaggerRepositoryComponent
import jp.kuaddo.tsuidezake.data.repository.RepositoryComponent
import jp.kuaddo.tsuidezake.di.module.ActivityModule
import jp.kuaddo.tsuidezake.di.module.AppModule
import jp.kuaddo.tsuidezake.di.module.ViewModelDelegateModule
import jp.kuaddo.tsuidezake.di.module.ViewModelModule

@AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        ViewModelDelegateModule::class,
        AppModule::class
    ],
    dependencies = [
        RepositoryComponent::class,
        AuthenticationComponent::class
    ]
)
interface AppComponent : AndroidInjector<TsuidezakeApp> {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: TsuidezakeApp,
            @BindsInstance applicationContext: Context,
            authenticationComponent: AuthenticationComponent =
                DaggerAuthenticationComponent.create(),
            repositoryComponent: RepositoryComponent =
                DaggerRepositoryComponent.factory().create(applicationContext)
        ): AppComponent
    }

    override fun inject(app: TsuidezakeApp)
}
