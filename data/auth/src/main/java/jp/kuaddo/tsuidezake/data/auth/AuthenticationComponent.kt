package jp.kuaddo.tsuidezake.data.auth

import dagger.Component
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationModule
import jp.kuaddo.tsuidezake.data.auth.internal.di.AuthenticationScope
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService

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
}
