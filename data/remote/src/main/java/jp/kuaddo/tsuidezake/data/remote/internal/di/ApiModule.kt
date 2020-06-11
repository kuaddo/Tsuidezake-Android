package jp.kuaddo.tsuidezake.data.remote.internal.di

import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.remote.internal.OAuthHeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Module
internal object ApiModule {
    @Provides
    @RemoteDataScope
    fun provideApollo(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
        .serverUrl("https://us-central1-tsuidezake.cloudfunctions.net/graphql")
        .okHttpClient(okHttpClient)
        .build()

    @Provides
    @RemoteDataScope
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(OAuthHeaderInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor(
                    object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) {
                            Timber.tag("OkHttp").d(message)
                        }
                    }
                ).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
}
