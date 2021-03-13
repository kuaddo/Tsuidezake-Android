package jp.kuaddo.tsuidezake.data.remote.internal

import com.apollographql.apollo.ApolloClient
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import jp.kuaddo.tsuidezake.data.remote.SAKE_DETAIL1
import jp.kuaddo.tsuidezake.data.remote.SAKE_DETAIL2
import jp.kuaddo.tsuidezake.data.remote.TsuidezakeDispatcher
import jp.kuaddo.tsuidezake.data.repository.SuccessResponse
import jp.kuaddo.tsuidezake.model.Ranking
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

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
        val mockedFirebaseImagePathConverter = mockk<FirebaseImagePathConverter> {
            val slot = slot<String>()
            coEvery { getImageUriString(capture(slot)) } answers { "converted:${slot.captured}" }
        }
        target = TsuidezakeServiceImpl(apolloClient, mockedFirebaseImagePathConverter)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetRankings() = runBlocking<Unit> {
        val rankings = target.getRankings()

        assertTrue(rankings is SuccessResponse)
        assertThat(rankings.data).containsExactly(
            Ranking(
                displayOrder = 1,
                category = "Total",
                contents = listOf(
                    Ranking.Content(rank = 1, sakeDetail = SAKE_DETAIL1),
                    Ranking.Content(rank = 2, sakeDetail = SAKE_DETAIL2),
                )
            ),
            Ranking(
                displayOrder = 2,
                category = "SecondaryRanking",
                contents = listOf(
                    Ranking.Content(rank = 1, sakeDetail = SAKE_DETAIL2),
                    Ranking.Content(rank = 2, sakeDetail = SAKE_DETAIL1),
                )
            )
        )
    }

    @Test
    fun testGetRecommendedSakes() = runBlocking<Unit> {
        val recommendedSakes = target.getRecommendedSakes()

        assertTrue(recommendedSakes is SuccessResponse)
        assertThat(recommendedSakes.data).containsExactly(
            Ranking.Content(rank = 1, sakeDetail = SAKE_DETAIL1),
            Ranking.Content(rank = 2, sakeDetail = SAKE_DETAIL2)
        )
    }

    @Test
    fun testGetWishList() = runBlocking<Unit> {
        val wishList = target.getWishList()

        assertTrue(wishList is SuccessResponse)
        assertThat(wishList.data).containsExactly(SAKE_DETAIL1, SAKE_DETAIL2)
    }

    @Test
    fun testGetSakeDetail() = runBlocking<Unit> {
        val sakeDetail = target.getSakeDetail(100)

        assertTrue(sakeDetail is SuccessResponse)
        assertThat(sakeDetail.data).isEqualTo(SAKE_DETAIL1)
    }
}
