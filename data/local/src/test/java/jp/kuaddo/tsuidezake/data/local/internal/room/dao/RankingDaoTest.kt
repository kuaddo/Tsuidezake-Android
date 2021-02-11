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
import jp.kuaddo.tsuidezake.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RankingDaoTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var rankingCategoryDao: RankingCategoryDao
    private lateinit var sakeDao: SakeDao
    private lateinit var rankingDao: RankingDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        rankingCategoryDao = database.rankingCategoryDao()
        sakeDao = database.sakeDao()
        rankingDao = database.rankingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testReplaceWith() = runBlocking<Unit> {
        rankingCategoryDao.insert(
            listOf(
                RankingCategoryEntity(name = "ranking1", order = 100),
                RankingCategoryEntity(name = "ranking2", order = 101),
            )
        )
        (1..3).map { EMPTY_SAKE_ENTITY.copy(sakeInfo = EMPTY_SAKE_INFO.copy(id = it)) }
            .forEach { sakeDao.upsertSakeEntity(it) }
        val rankings = (1..3).map { RankingEntity(categoryId = 1, rank = it, sakeId = it) }
        val modifiedRankings = rankings.map { it.copy(categoryId = 2) }
        rankingDao.insert(rankings)
        assertThat(rankingDao.selectAll().toSet()).containsExactlyElementsOf(rankings)

        rankingDao.replaceWith(modifiedRankings.toSet())

        assertThat(rankingDao.selectAll().toSet()).containsExactlyElementsOf(modifiedRankings)
    }
}
