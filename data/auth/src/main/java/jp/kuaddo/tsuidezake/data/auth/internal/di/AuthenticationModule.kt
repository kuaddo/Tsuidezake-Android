package jp.kuaddo.tsuidezake.data.auth.internal.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.auth.internal.AuthServiceImpl
import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.repository.AuthService

@Module
internal abstract class AuthenticationModule {
    @Suppress("unused")
    @Binds
    @AuthenticationScope
    abstract fun bindAuthToken(impl: AuthServiceImpl): AuthToken

    @Suppress("unused")
    @Binds
    @AuthenticationScope
    abstract fun bindAuthService(impl: AuthServiceImpl): AuthService

    companion object {
        @Provides
        @AuthenticationScope
        fun provideFirebase(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
