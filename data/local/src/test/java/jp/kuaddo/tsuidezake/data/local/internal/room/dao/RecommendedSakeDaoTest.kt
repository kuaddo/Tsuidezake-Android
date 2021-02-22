package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_ENTITY
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_INFO
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity
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
class RecommendedSakeDaoTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var sakeDao: SakeDao
    private lateinit var recommendedSakeDao: RecommendedSakeDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        sakeDao = database.sakeDao()
        recommendedSakeDao = database.recommendedSakeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testFindAll() = runBlocking<Unit> {
        val sakes = (0..3).map { EMPTY_SAKE_ENTITY.copy(sakeInfo = EMPTY_SAKE_INFO.copy(id = it)) }
        val recommendedSakes = (0..3).map { RecommendedSakeEntity(order = it, sakeId = it) }
        sakes.forEach { sakeDao.upsertSakeEntity(it) }

        recommendedSakeDao.insert(recommendedSakes)

        assertThat(recommendedSakeDao.findAll().first())
            .containsExactlyElementsOf(recommendedSakes)
    }

    @Test
    fun testReplaceWith() = runBlocking<Unit> {
        val sakes = (0..3).map { EMPTY_SAKE_ENTITY.copy(sakeInfo = EMPTY_SAKE_INFO.copy(id = it)) }
        val recommendedSakes = (0..3).map { RecommendedSakeEntity(order = it, sakeId = it) }
        val modifiedRecommendedSakes = recommendedSakes.map { it.copy(order = 3 - it.order) }
        sakes.forEach { sakeDao.upsertSakeEntity(it) }
        recommendedSakeDao.insert(recommendedSakes)
        assertThat(recommendedSakeDao.findAll().first().toSet())
            .containsOnlyOnceElementsOf(recommendedSakes)

        recommendedSakeDao.replaceWith(modifiedRecommendedSakes.toSet())

        assertThat(recommendedSakeDao.findAll().first().toSet())
            .containsOnlyOnceElementsOf(modifiedRecommendedSakes)
    }
}
