package jp.kuaddo.tsuidezake.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
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