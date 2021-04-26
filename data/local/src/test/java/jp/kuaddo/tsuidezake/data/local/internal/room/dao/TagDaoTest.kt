package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TagDaoTest {
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var database: TsuidezakeDB
    private lateinit var tagDao: TagDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TsuidezakeDB::class.java)
            .allowMainThreadQueries()
            .build()
        tagDao = database.tagDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun failedTest() {
        fail("danger test!!!")
    }

    @Test
    fun testUpsert_insert() = runBlocking<Unit> {
        val tags = (0..3).map { TagEntity(it, "name$it") }

        tagDao.upsert(tags.toSet())

        assertThat(tagDao.findAll()).containsOnlyOnceElementsOf(tags)
    }

    @Test
    fun testUpsert_update() = runBlocking<Unit> {
        val tags = (0..3).map { TagEntity(it, "name$it") }
        val modifiedTags = tags.map { it.copy(name = "modified_${it.name}") }
        tagDao.upsert(tags.toSet())

        tagDao.upsert(modifiedTags.toSet())

        assertThat(tagDao.findAll()).containsOnlyOnceElementsOf(modifiedTags)
    }

    @Test
    fun testUpsert_insertAndUpdate() = runBlocking<Unit> {
        val tags = (0..3).map { TagEntity(it, "name$it") }
        val modifiedTags = (2..3).map { TagEntity(it, "modified_name$it") }
        val insertedTags = (4..5).map { TagEntity(it, "inserted_name$it") }
        tagDao.upsert(tags.toSet())

        tagDao.upsert((modifiedTags + insertedTags).toSet())

        val expected = (0..1).map { TagEntity(it, "name$it") } +
            (2..3).map { TagEntity(it, "modified_name$it") } +
            (4..5).map { TagEntity(it, "inserted_name$it") }
        assertThat(tagDao.findAll()).containsOnlyOnceElementsOf(expected)
    }
}
