package jp.kuaddo.tsuidezake.data.remote.internal

import jp.kuaddo.tsuidezake.data.remote.AuthToken
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@RemoteDataScope
internal class OAuthHeaderInterceptor @Inject constructor(
    private val authToken: AuthToken
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
        authToken.token?.let { requestBuilder.addHeader("Authorization", it) }
        return chain.proceed(requestBuilder.build())
    }
}
