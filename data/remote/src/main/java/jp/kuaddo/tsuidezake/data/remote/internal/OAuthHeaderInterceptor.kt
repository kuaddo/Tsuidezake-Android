package jp.kuaddo.tsuidezake.data.remote.internal

import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@RemoteDataScope
internal class OAuthHeaderInterceptor @Inject constructor(
    private val authService: AuthService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
        authService.token?.let { requestBuilder.addHeader("Authorization", it) }
        return chain.proceed(requestBuilder.build())
    }
}
