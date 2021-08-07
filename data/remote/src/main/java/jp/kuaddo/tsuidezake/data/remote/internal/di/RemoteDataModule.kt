package jp.kuaddo.tsuidezake.data.remote.internal.di

import com.apollographql.apollo.ApolloClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import jp.kuaddo.tsuidezake.data.remote.internal.OAuthHeaderInterceptor
import jp.kuaddo.tsuidezake.data.remote.internal.TsuidezakeServiceImpl
import jp.kuaddo.tsuidezake.data.repository.TsuidezakeService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Module
@DisableInstallInCheck
internal abstract class RemoteDataModule {
    @Binds
    abstract fun bindTsuidezakeService(impl: TsuidezakeServiceImpl): TsuidezakeService

    companion object {
        @Provides
        @RemoteDataScope
        fun provideApollo(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
            .serverUrl("https://us-central1-tsuidezake.cloudfunctions.net/graphql")
            .okHttpClient(okHttpClient)
            .build()

        @Provides
        @RemoteDataScope
        fun provideOkHttpClient(headerInterceptor: OAuthHeaderInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        Timber.tag("OkHttp").d(message)
                    }.apply { level = HttpLoggingInterceptor.Level.BODY }
                ).build()
    }
}
