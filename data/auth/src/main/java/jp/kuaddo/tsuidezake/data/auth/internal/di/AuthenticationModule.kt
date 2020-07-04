package jp.kuaddo.tsuidezake.data.auth.internal.di

import dagger.Binds
import dagger.Module
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.data.auth.internal.AuthServiceImpl

@Module
internal abstract class AuthenticationModule {
    @Binds
    abstract fun bindAuthService(impl: AuthServiceImpl): AuthService
}
