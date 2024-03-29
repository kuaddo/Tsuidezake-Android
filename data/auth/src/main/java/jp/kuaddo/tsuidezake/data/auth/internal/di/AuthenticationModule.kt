package jp.kuaddo.tsuidezake.data.auth.internal.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import jp.kuaddo.tsuidezake.data.auth.internal.AuthServiceImpl
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService

@Module
@DisableInstallInCheck
internal abstract class AuthenticationModule {
    @Binds
    @AuthenticationScope
    abstract fun bindAuthToken(impl: AuthServiceImpl): AuthToken

    @Binds
    @AuthenticationScope
    abstract fun bindAuthService(impl: AuthServiceImpl): AuthService

    companion object {
        @Provides
        @AuthenticationScope
        fun provideFirebase(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
