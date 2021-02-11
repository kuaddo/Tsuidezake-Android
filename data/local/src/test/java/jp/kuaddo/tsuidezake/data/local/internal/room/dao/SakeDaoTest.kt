package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.kuaddo.tsuidezake.data.local.EMPTY_ROOM_SAKE
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_ENTITY
import jp.kuaddo.tsuidezake.data.local.EMPTY_SAKE_INFO
import jp.kuaddo.tsuidezake.data.local.EMPTY_WISH_UPDATE
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SuitableTemperature
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
class SakeDaoTest {
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
    fun testMappingToRoomSake() = runBlocking<Unit> {
        val tags = (0..2).map { TagEntity(id = it, name = "tag$it") }
        sakeDao.upsertSakeEntity(EMPTY_SAKE_ENTITY)
        tagDao.upsert(tags.toSet())
        sakeTagDao.insertIfAbsent(
            setOf(
                SakeTagCrossRef(sakeId = 0, tagId = 0),
                SakeTagCrossRef(sakeId = 0, tagId = 2)
            )
        )

        val roomSake = sakeDao.selectAll().first()

        val expected = RoomSake(
            EMPTY_SAKE_ENTITY,
            listOf(tags[0], tags[2])
        )
        assertThat(roomSake).isEqualTo(expected)
    }

    @Test
    fun testSelectWishList() = runBlocking<Unit> {
        val sakes = (0..3).map { id ->
            EMPTY_SAKE_ENTITY.copy(
                sakeInfo = EMPTY_SAKE_ENTITY.sakeInfo.copy(id = id),
                isAddedToWish = id % 2 == 0
            )
        }
        sakes.forEach { sakeDao.upsertSakeEntity(it) }

        val result = sakeDao.selectWishList().first()

        val expected = sakes.filter { it.isAddedToWish }
            .map { RoomSake(it, emptyList()) }
        assertThat(result).containsExactlyElementsOf(expected)
    }

    @Test
    fun testFindById() = runBlocking<Unit> {
        sakeDao.upsertSakeEntity(EMPTY_SAKE_ENTITY)

        assertThat(sakeDao.findById(0).first()).isEqualTo(EMPTY_ROOM_SAKE)
    }

    @Test
    fun testUpsertSakeEntity_insert() = runBlocking<Unit> {
        sakeDao.upsertSakeEntity(EMPTY_SAKE_ENTITY)

        val expected = listOf(EMPTY_ROOM_SAKE)
        assertThat(sakeDao.selectAll()).containsExactlyElementsOf(expected)
    }

    @Test
    fun testUpsertSakeEntity_update() = runBlocking<Unit> {
        val updatedSake = EMPTY_SAKE_ENTITY.copy(
            sakeInfo = EMPTY_SAKE_INFO.copy(
                name = "newName",
                description = "newDescription",
                region = "newRegion",
                brewer = "newBrewer",
                imageUri = "newImageUri",
                suitableTemperatures = setOf(SuitableTemperature.ROCK, SuitableTemperature.NORMAL),
                goodFoodCategories = setOf(FoodCategory.DAIRY, FoodCategory.SNACK),
            ),
            isAddedToWish = true,
            isAddedToTasted = true
        )
        sakeDao.upsertSakeEntity(EMPTY_SAKE_ENTITY)

        sakeDao.upsertSakeEntity(updatedSake)

        val expected = listOf(RoomSake(updatedSake, emptyList()))
        assertThat(sakeDao.selectAll()).containsExactlyElementsOf(expected)
    }

    @Test
    fun testUpsertSakeInfo_insert() = runBlocking<Unit> {
        sakeDao.upsertSakeInfo(EMPTY_SAKE_INFO)

        val expected = listOf(EMPTY_ROOM_SAKE)
        assertThat(sakeDao.selectAll()).containsExactlyElementsOf(expected)
    }

    @Test
    fun testUpsertSakeInfo_update() = runBlocking<Unit> {
        val sakeInfo = EMPTY_SAKE_INFO.copy(
            name = "newName",
            description = "newDescription",
            region = "newRegion",
            brewer = "newBrewer",
            imageUri = "newImageUri",
            suitableTemperatures = setOf(SuitableTemperature.ROCK, SuitableTemperature.NORMAL),
            goodFoodCategories = setOf(FoodCategory.DAIRY, FoodCategory.SNACK)
        )
        sakeDao.upsertSakeEntity(
            EMPTY_SAKE_ENTITY.copy(isAddedToWish = true, isAddedToTasted = true)
        )

        sakeDao.upsertSakeInfo(sakeInfo)

        val expected = listOf(
            RoomSake(
                EMPTY_SAKE_ENTITY.copy(
                    sakeInfo = sakeInfo,
                    isAddedToWish = true,
                    isAddedToTasted = true
                ),
                emptyList()
            )
        )
        assertThat(sakeDao.selectAll()).containsExactlyElementsOf(expected)
    }

    @Test
    fun testUpsertSakeInfos() = runBlocking<Unit> {
        val sakes = (0..3).map { id ->
            EMPTY_SAKE_ENTITY.copy(
                sakeInfo = EMPTY_SAKE_ENTITY.sakeInfo.copy(id = id),
                isAddedToWish = true,
                isAddedToTasted = true
            )
        }
        sakes.forEach { sakeDao.upsertSakeEntity(it) }
        val sakeInfos = (2..5).map { id ->
            EMPTY_SAKE_INFO.copy(
                id = id,
                name = "newName$id",
                description = "newDescription$id",
                region = "newRegion$id",
                brewer = "newBrewer$id",
                imageUri = "newImageUri$id",
                suitableTemperatures = setOf(SuitableTemperature.ROCK, SuitableTemperature.NORMAL),
                goodFoodCategories = setOf(FoodCategory.DAIRY, FoodCategory.SNACK)
            )
        }

        sakeDao.upsertSakeInfos(sakeInfos.toSet())

        val expected = listOf(
            *sakes.take(2).map { RoomSake(it, emptyList()) }.toTypedArray(),
            *sakeInfos.take(2).map { sakeInfo ->
                RoomSake(
                    EMPTY_SAKE_ENTITY.copy(
                        sakeInfo = sakeInfo,
                        isAddedToWish = true,
                        isAddedToTasted = true
                    ),
                    emptyList()
                )
            }.toTypedArray(),
            *sakeInfos.drop(2).map { sakeInfo ->
                RoomSake(
                    EMPTY_SAKE_ENTITY.copy(
                        sakeInfo = sakeInfo,
                        isAddedToWish = false,
                        isAddedToTasted = false
                    ),
                    emptyList()
                )
            }.toTypedArray()
        )
        assertThat(sakeDao.selectAll()).isEqualTo(expected)
    }

    @Test
    fun testUpsertWishUpdates() = runBlocking<Unit> {
        val sakes = (0..3).map { id ->
            EMPTY_SAKE_ENTITY.copy(sakeInfo = EMPTY_SAKE_ENTITY.sakeInfo.copy(id = id))
        }
        sakes.forEach { sakeDao.upsertSakeEntity(it) }
        val wishUpdates = (2..5).map { id ->
            val sakeInfo = EMPTY_SAKE_INFO.copy(
                id = id,
                name = "newName$id",
                description = "newDescription$id",
                region = "newRegion$id",
                brewer = "newBrewer$id",
                imageUri = "newImageUri$id",
                suitableTemperatures = setOf(SuitableTemperature.ROCK, SuitableTemperature.NORMAL),
                goodFoodCategories = setOf(FoodCategory.DAIRY, FoodCategory.SNACK)
            )
            EMPTY_WISH_UPDATE.copy(sakeInfo, isAddedToWish = true)
        }

        sakeDao.upsertWishUpdates(wishUpdates.toSet())

        val expected = listOf(
            *sakes.take(2).map { RoomSake(it, emptyList()) }.toTypedArray(),
            *wishUpdates.take(2).map { wishUpdate ->
                RoomSake(
                    EMPTY_SAKE_ENTITY.copy(
                        sakeInfo = wishUpdate.sakeInfo,
                        isAddedToWish = wishUpdate.isAddedToWish
                    ),
                    emptyList()
                )
            }.toTypedArray(),
            *wishUpdates.drop(2).map { wishUpdate ->
                RoomSake(
                    EMPTY_SAKE_ENTITY.copy(
                        sakeInfo = wishUpdate.sakeInfo,
                        isAddedToWish = wishUpdate.isAddedToWish
                    ),
                    emptyList()
                )
            }.toTypedArray()
        )
        assertThat(sakeDao.selectAll()).isEqualTo(expected)
    }
}
