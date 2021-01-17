package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SakeTagDaoTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var sakeDao: SakeDao
    private lateinit var tagDao: TagDao
    private lateinit var sakeTagDao: SakeTagDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        sakeDao = database.sakeDao()
        tagDao = database.tagDao()
        sakeTagDao = database.sakeTagDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertIfAbsent() = runBlocking<Unit> {
        val existing = (0..2).map { SakeTagCrossRef(it, it * 10) }.toSet()
        val sakeTagCrossRefs = (1..3).map { SakeTagCrossRef(it, it * 10) }.toSet()
        (sakeTagCrossRefs + existing).forEach { sakeTagCrossRef ->
            insertDummySake(sakeTagCrossRef.sakeId)
            insertDummyTag(sakeTagCrossRef.tagId)
        }
        sakeTagDao.insertIfAbsent(existing)

        sakeTagDao.insertIfAbsent(sakeTagCrossRefs)

        assertThat(sakeTagDao.findAll()).containsOnlyOnceElementsOf(existing + sakeTagCrossRefs)
    }

    private suspend fun insertDummySake(id: Int) {
        sakeDao.upsertSakeEntity(EMPTY_SAKE_ENTITY.copy(id = id))
    }

    private suspend fun insertDummyTag(id: Int) {
        tagDao.upsert(setOf(TagEntity(id, "")))
    }

    companion object {
        private val EMPTY_SAKE_ENTITY = SakeEntity(
            id = 0,
            name = "",
            description = null,
            region = "",
            brewer = null,
            imageUri = null,
            suitableTemperatures = emptySet(),
            goodFoodCategories = emptySet(),
            isAddedToWish = false,
            isAddedToTasted = false
        )
    }
}
