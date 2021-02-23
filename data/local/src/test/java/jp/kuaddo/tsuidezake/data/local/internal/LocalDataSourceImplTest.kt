package jp.kuaddo.tsuidezake.data.local.internal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import jp.kuaddo.tsuidezake.model.Tag
import jp.kuaddo.tsuidezake.model.UserSake
import jp.kuaddo.tsuidezake.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LocalDataSourceImplTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        localDataSourceImpl = LocalDataSourceImpl(
            database,
            database.sakeDao(),
            database.tagDao(),
            database.recommendedSakeDao(),
            database.rankingDao(),
            database.rankingCategoryDao(),
            database.sakeTagDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testLoadUserSakeFlow() = runBlocking<Unit> {
        USER_SAKE_LIST_TEST_DATA.forEach { localDataSourceImpl.saveUserSake(it) }

        val userSake = localDataSourceImpl.loadUserSakeFlow(sakeId = 2).first()

        assertThat(userSake).isEqualTo(USER_SAKE_LIST_TEST_DATA[1])
    }

    @Test
    fun testLoadUserSakeFlow_specifyNonExistingId() = runBlocking {
        USER_SAKE_LIST_TEST_DATA.forEach { localDataSourceImpl.saveUserSake(it) }

        val userSake = localDataSourceImpl.loadUserSakeFlow(sakeId = 100).first()

        assertThat(userSake).isNull()
    }

    @Test
    fun testLoadSakeDetailFlow() = runBlocking<Unit> {
        USER_SAKE_LIST_TEST_DATA.forEach { localDataSourceImpl.saveUserSake(it) }

        val sakeDetail = localDataSourceImpl.loadSakeDetailFlow(sakeId = 1).first()

        assertThat(sakeDetail).isEqualTo(USER_SAKE_LIST_TEST_DATA[0].sakeDetail)
    }

    @Test
    fun testLoadSakeDetailFlow_specifyNonExistingId() = runBlocking {
        USER_SAKE_LIST_TEST_DATA.forEach { localDataSourceImpl.saveUserSake(it) }

        val sakeDetail = localDataSourceImpl.loadSakeDetailFlow(sakeId = 100).first()

        assertThat(sakeDetail).isNull()
    }

    @Test
    fun testLoadWishListFlow() = runBlocking<Unit> {
        USER_SAKE_LIST_TEST_DATA.forEach { localDataSourceImpl.saveUserSake(it) }

        val wishList = localDataSourceImpl.loadWishListFlow().first()

        val expected = USER_SAKE_LIST_TEST_DATA
            .filter(UserSake::isAddedToWish)
            .map(UserSake::sakeDetail)
        assertThat(wishList).containsExactlyElementsOf(expected)
    }

    @Test
    fun testLoadWishListFlow_wishListIsEmpty() = runBlocking {
        USER_SAKE_LIST_TEST_DATA
            .map { it.copy(isAddedToWish = false) }
            .forEach { localDataSourceImpl.saveUserSake(it) }

        val wishList = localDataSourceImpl.loadWishListFlow().first()

        assertThat(wishList).isEmpty()
    }

    @Test
    fun testLoadRankingsFlow() = runBlocking<Unit> {
        localDataSourceImpl.saveRankings(RANKING_LIST_TEST_DATA)

        val rankings = localDataSourceImpl.loadRankingsFlow().first()

        assertThat(rankings).containsExactlyElementsOf(RANKING_LIST_TEST_DATA)
    }

    @Test
    fun testLoadRankingsFlow_contentsIsEmpty() = runBlocking<Unit> {
        val rankings = (1..3).map { id ->
            Ranking(
                displayOrder = id,
                category = "category$id",
                contents = emptyList()
            )
        }
        localDataSourceImpl.saveRankings(rankings)

        assertThat(localDataSourceImpl.loadRankingsFlow().first())
            .containsExactlyElementsOf(rankings)
    }

    @Test
    fun testLoadRankingsFlow_rankingsIsEmpty() = runBlocking {
        assertThat(localDataSourceImpl.loadRankingsFlow().first()).isEmpty()
    }

    companion object {
        private val USER_SAKE_LIST_TEST_DATA: List<UserSake> = (1..3).map { id ->
            UserSake(
                sakeDetail = SakeDetail(
                    id = id,
                    name = "name$id",
                    description = "description$id",
                    region = "region$id",
                    brewer = "brewer$id",
                    imageUri = "imageUri$id",
                    tags = (1..id).map { tagId -> Tag(id = tagId, name = "tag$tagId") },
                    suitableTemperatures = setOf(
                        SuitableTemperature.COLD,
                        SuitableTemperature.WARM
                    ),
                    goodFoodCategories = setOf(FoodCategory.SEAFOOD, FoodCategory.SNACK)
                ),
                isAddedToWish = id >= 2,
                isAddedToTasted = id >= 2
            )
        }
        private val CONTENTS_LIST_TEST_DATA: List<List<Ranking.Content>> = (1..3).map { size ->
            (1..size).map { rank ->
                Ranking.Content(
                    rank = rank,
                    sakeDetail = USER_SAKE_LIST_TEST_DATA[rank - 1].sakeDetail
                )
            }
        }
        private val RANKING_LIST_TEST_DATA: List<Ranking> = (1..3).map { id ->
            Ranking(
                displayOrder = id,
                category = "category$id",
                contents = CONTENTS_LIST_TEST_DATA[id - 1]
            )
        }
    }
}
