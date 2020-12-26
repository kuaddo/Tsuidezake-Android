package jp.kuaddo.tsuidezake.data.local.internal

import androidx.room.withTransaction
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.RecommendedSakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeTagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.TagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.WishUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class LocalDataSourceImpl @Inject constructor(
    private val db: TsuidezakeDB,
    private val sakeDao: SakeDao,
    private val tagDao: TagDao,
    private val recommendedSakeDao: RecommendedSakeDao,
    private val sakeTagDao: SakeTagDao
) : LocalDataSource {
    override fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?> =
        sakeDao.findById(sakeId).map { it?.toUserSake() }.flowOn(Dispatchers.IO)

    override fun loadSakeDetailFlow(sakeId: Int): Flow<SakeDetail?> =
        sakeDao.findById(sakeId).map { it?.toSakeDetail() }.flowOn(Dispatchers.IO)

    override fun loadWishList(): Flow<List<SakeDetail>> = sakeDao.selectWishList()
        .map { roomSakeList -> roomSakeList.map(RoomSake::toSakeDetail) }
        .flowOn(Dispatchers.IO)

    override fun loadRecommendedSakes(): Flow<List<Ranking.Content>> =
        recommendedSakeDao.findAll().flatMapLatest { recommendedSakes ->
            if (recommendedSakes.isEmpty()) {
                return@flatMapLatest flowOf(emptyList())
            }

            val contentFlowList = recommendedSakes.map(::loadContent)
            combine(contentFlowList) { it.toList() }
        }

    private fun loadContent(recommendedSake: RecommendedSakeEntity): Flow<Ranking.Content> =
        sakeDao.findById(recommendedSake.sakeId).mapNotNull { roomSake ->
            roomSake?.let {
                Ranking.Content(
                    rank = recommendedSake.order,
                    sakeDetail = it.toSakeDetail()
                )
            }
        }

    override suspend fun saveUserSake(userSake: UserSake) = withContext(Dispatchers.IO) {
        val sakeEntity = SakeEntity.of(userSake)
        val tagEntities = userSake.sakeDetail.tags.map(TagEntity::of).toSet()
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(userSake.sakeDetail).toSet()

        db.withTransaction {
            sakeDao.upsertSakeEntity(sakeEntity)
            tagDao.upsert(tagEntities)
            sakeTagDao.upsert(sakeTagCrossRefs)
        }
    }

    override suspend fun saveSakeDetail(sakeDetail: SakeDetail) = withContext(Dispatchers.IO) {
        val sakeUpdate = SakeUpdate.of(sakeDetail)
        val tagEntities = sakeDetail.tags.map(TagEntity::of).toSet()
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(sakeDetail).toSet()

        db.withTransaction {
            sakeDao.upsertSakeUpdate(sakeUpdate)
            tagDao.upsert(tagEntities)
            sakeTagDao.upsert(sakeTagCrossRefs)
        }
    }

    override suspend fun saveWishList(wishList: List<SakeDetail>) = withContext(Dispatchers.IO) {
        val wishUpdates = wishList.map { WishUpdate.of(it, true) }.toSet()
        val tagEntities = wishList.flatMap { it.tags.map(TagEntity::of) }.toSet()
        val sakeTagCrossRefs = wishList.flatMap(SakeTagCrossRef::createSakeTagCrossRefs).toSet()

        db.withTransaction {
            sakeDao.upsertWishUpdates(wishUpdates)
            tagDao.upsert(tagEntities)
            sakeTagDao.upsert(sakeTagCrossRefs)
        }
    }

    override suspend fun saveRecommendedSakes(
        contents: List<Ranking.Content>
    ) = withContext(Dispatchers.IO) {
        val sakeUpdates = contents.map { SakeUpdate.of(it.sakeDetail) }.toSet()
        val recommendedList = contents.map(RecommendedSakeEntity::of).toSet()

        db.withTransaction {
            sakeDao.upsertSakeUpdates(sakeUpdates)
            recommendedSakeDao.replaceWith(recommendedList)
        }
    }
}
