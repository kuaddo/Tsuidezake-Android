package jp.kuaddo.tsuidezake.data.auth.internal.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.data.auth.internal.AuthServiceImpl

@Module
internal abstract class AuthenticationModule {
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
