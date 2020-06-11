package jp.kuaddo.tsuidezake.data.remote.internal

import okhttp3.Interceptor
import okhttp3.Response

internal class OAuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .header("Authorization", "dummy token")
                .build()
        )
    }
}
