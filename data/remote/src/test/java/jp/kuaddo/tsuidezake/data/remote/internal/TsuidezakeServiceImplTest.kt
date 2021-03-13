package jp.kuaddo.tsuidezake.data.remote.internal

import com.apollographql.apollo.ApolloClient
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeDispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

class TsuidezakeServiceImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var target: TsuidezakeServiceImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            dispatcher = TsuidezakeDispatcher()
            start()
        }
        val apolloClient = ApolloClient.builder()
            .serverUrl(mockWebServer.url(""))
            .okHttpClient(OkHttpClient())
            .build()
        target = TsuidezakeServiceImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
