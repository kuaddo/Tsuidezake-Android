package jp.kuaddo.tsuidezake.data.auth

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationModule
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationScope
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService
import javax.inject.Singleton

@AuthenticationScope
@Component(
    modules = [AuthenticationModule::class]
)
interface AuthenticationComponent {
    val authToken: AuthToken
    val authService: AuthService

    @Component.Factory
    interface Factory {
        fun create(): AuthenticationComponent
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideAuthenticationComponent(): AuthenticationComponent =
            DaggerAuthenticationComponent.create()

        @Provides
        fun provideAuthToken(component: AuthenticationComponent): AuthToken =
            component.authToken

        @Provides
        fun provideAuthService(component: AuthenticationComponent): AuthService =
            component.authService
    }
}
