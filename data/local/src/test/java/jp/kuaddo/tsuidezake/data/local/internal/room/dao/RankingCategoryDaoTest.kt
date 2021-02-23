package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_ENTITY
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_INFO
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomRanking
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
class RankingCategoryDaoTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var sakeDao: SakeDao
    private lateinit var rankingDao: RankingDao
    private lateinit var rankingCategoryDao: RankingCategoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        sakeDao = database.sakeDao()
        rankingDao = database.rankingDao()
        rankingCategoryDao = database.rankingCategoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testFindAll() = runBlocking<Unit> {
        val rankingCategories = (1..2).map {
            RankingCategoryEntity(id = it.toLong(), name = "category$it", order = it)
        }
        rankingCategoryDao.insert(rankingCategories)
        (1..3).map { EMPTY_SAKE_ENTITY.copy(sakeInfo = EMPTY_SAKE_INFO.copy(id = it)) }
            .forEach { sakeDao.upsertSakeEntity(it) }
        val category1Rankings = (1..3).map {
            RankingEntity(categoryId = 1, rank = it, sakeId = it)
        }
        val category2Rankings = (1..3).map {
            RankingEntity(categoryId = 2, rank = it, sakeId = it)
        }
        rankingDao.insert(category1Rankings)
        rankingDao.insert(category2Rankings)

        val rankings = rankingCategoryDao.findAll().first()

        val expected = listOf(
            RoomRanking(
                rankingCategoryEntity = rankingCategories[0],
                rankingEntities = category1Rankings
            ),
            RoomRanking(
                rankingCategoryEntity = rankingCategories[1],
                rankingEntities = category2Rankings
            )
        )
        assertThat(rankings).containsExactlyElementsOf(expected)
    }

    @Test
    fun testSelectIdBy() = runBlocking<Unit> {
        (1..3).map { RankingCategoryEntity(id = it.toLong(), name = "category$it", order = it) }
            .let { rankingCategoryDao.insert(it) }

        val rankingId = rankingCategoryDao.selectIdBy("category2")

        assertThat(rankingId).isEqualTo(2)
    }

    @Test
    fun testReplaceWith() = runBlocking<Unit> {
        val rankingCategories = (1..3).map {
            RankingCategoryEntity(id = it.toLong(), name = "category$it", order = it)
        }
        rankingCategoryDao.insert(rankingCategories)
        val modifiedRankingCategories = rankingCategories.map {
            it.copy(name = "newCategory${it.id}")
        }
        assertThat(rankingCategoryDao.selectAll().toSet())
            .containsExactlyElementsOf(rankingCategories)

        rankingCategoryDao.replaceWith(modifiedRankingCategories.toSet())

        assertThat(rankingCategoryDao.selectAll().toSet())
            .containsExactlyElementsOf(modifiedRankingCategories)
    }
}
