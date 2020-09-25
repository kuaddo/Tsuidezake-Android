package jp.kuaddo.tsuidezake.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import jp.kuaddo.tsuidezake.TsuidezakeApp
import jp.kuaddo.tsuidezake.data.auth.DaggerAuthenticationComponent
import jp.kuaddo.tsuidezake.data.local.DaggerLocalDataComponent
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.remote.DaggerRemoteDataComponent
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
        RepositoryComponent::class
        // TODO: create use case component and depend it only
    ]
)
interface AppComponent : AndroidInjector<TsuidezakeApp> {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: TsuidezakeApp,
            @BindsInstance applicationContext: Context = application.applicationContext,
            repositoryComponent: RepositoryComponent =
                createRepositoryComponent(applicationContext)
        ): AppComponent
    }

    override fun inject(app: TsuidezakeApp)
}

private fun createRepositoryComponent(applicationContext: Context): RepositoryComponent {
    val authenticationComponent = createAuthenticationComponent()
    return DaggerRepositoryComponent.factory().create(
        createLocalDataComponent(applicationContext).preferenceStorage,
        createRemoteDataComponent(authenticationComponent.authToken).tsuidezakeService,
        authenticationComponent.authService
    )
}

private fun createAuthenticationComponent() = DaggerAuthenticationComponent.create()

private fun createLocalDataComponent(applicationContext: Context) =
    DaggerLocalDataComponent.factory().create(applicationContext)

private fun createRemoteDataComponent(authToken: AuthToken) =
    DaggerRemoteDataComponent.factory().create(authToken)
