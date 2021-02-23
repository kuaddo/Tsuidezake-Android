package jp.kuaddo.tsuidezake.data.local.internal

import androidx.room.withTransaction
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.RankingCategoryDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.RankingDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.RecommendedSakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeTagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.TagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeInfo
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.WishUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomRanking
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@Suppress("EXPERIMENTAL_API_USAGE")
internal class LocalDataSourceImpl @Inject constructor(
    private val db: TsuidezakeDB,
    private val sakeDao: SakeDao,
    private val tagDao: TagDao,
    private val recommendedSakeDao: RecommendedSakeDao,
    private val rankingDao: RankingDao,
    private val rankingCategoryDao: RankingCategoryDao,
    private val sakeTagDao: SakeTagDao
) : LocalDataSource {
    override fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?> =
        sakeDao.findById(sakeId).map { it?.toUserSake() }

    override fun loadSakeDetailFlow(sakeId: Int): Flow<SakeDetail?> =
        sakeDao.findById(sakeId).map { it?.toSakeDetail() }

    override fun loadWishListFlow(): Flow<List<SakeDetail>> = sakeDao.selectWishList()
        .map { roomSakes -> roomSakes.map(RoomSake::toSakeDetail) }

    override fun loadRankingsFlow(): Flow<List<Ranking>> = rankingCategoryDao.findAll()
        .flatMapLatest { roomRankings ->
            val rankingFlows = roomRankings.map(::loadRanking)
            combineWithEmpty(rankingFlows) { it.toList() }
        }

    private fun loadRanking(roomRanking: RoomRanking): Flow<Ranking> {
        val contentFlows = roomRanking.rankingEntities.map { loadContent(it.sakeId, it.rank) }
        return combineWithEmpty(contentFlows) { it.toList() }
            .map { contents ->
                Ranking(
                    displayOrder = roomRanking.rankingCategoryEntity.order,
                    category = roomRanking.rankingCategoryEntity.name,
                    contents = contents
                )
            }
    }

    override fun loadRecommendedSakesFlow(): Flow<List<Ranking.Content>> =
        recommendedSakeDao.findAll().flatMapLatest { recommendedSakes ->
            val contentFlows = recommendedSakes.map { loadContent(it.sakeId, it.order) }
            combineWithEmpty(contentFlows) { it.toList() }
        }

    private fun loadContent(sakeId: Int, rank: Int): Flow<Ranking.Content> =
        sakeDao.findById(sakeId).mapNotNull { roomSake ->
            roomSake?.let { Ranking.Content(rank = rank, sakeDetail = it.toSakeDetail()) }
        }

    override suspend fun saveUserSake(userSake: UserSake) {
        val sakeEntity = SakeEntity.of(userSake)
        val tagEntities = userSake.sakeDetail.tags.map(TagEntity::of).toSet()
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(userSake.sakeDetail).toSet()

        db.withTransaction {
            sakeDao.upsertSakeEntity(sakeEntity)
            tagDao.upsert(tagEntities)
            sakeTagDao.insertIfAbsent(sakeTagCrossRefs)
        }
    }

    override suspend fun saveSakeDetail(sakeDetail: SakeDetail) {
        val sakeInfo = SakeInfo.of(sakeDetail)
        val tagEntities = sakeDetail.tags.map(TagEntity::of).toSet()
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(sakeDetail).toSet()

        db.withTransaction {
            sakeDao.upsertSakeInfo(sakeInfo)
            tagDao.upsert(tagEntities)
            sakeTagDao.insertIfAbsent(sakeTagCrossRefs)
        }
    }

    override suspend fun saveWishList(wishList: List<SakeDetail>) {
        val wishUpdates = wishList.map { WishUpdate.of(it, true) }.toSet()
        val tagEntities = wishList.flatMap { it.tags.map(TagEntity::of) }.toSet()
        val sakeTagCrossRefs = wishList.flatMap(SakeTagCrossRef::createSakeTagCrossRefs).toSet()

        db.withTransaction {
            sakeDao.upsertWishUpdates(wishUpdates)
            tagDao.upsert(tagEntities)
            sakeTagDao.insertIfAbsent(sakeTagCrossRefs)
        }
    }

    override suspend fun replaceRankings(rankings: List<Ranking>) {
        val rankingCategoryEntities = rankings.map(RankingCategoryEntity::of).toSet()
        val sakeDetails = rankings.flatMap { ranking ->
            ranking.contents.map(Ranking.Content::sakeDetail)
        }

        db.withTransaction {
            rankingCategoryDao.replaceWith(rankingCategoryEntities)
            sakeDetails.forEach { saveSakeDetail(it) }

            val rankingEntities = rankings.flatMap { ranking ->
                val categoryId = rankingCategoryDao.selectIdBy(ranking.category)
                ranking.contents.map { RankingEntity.of(categoryId, it) }
            }.toSet()
            rankingDao.replaceWith(rankingEntities)
        }
    }

    override suspend fun replaceRecommendedSakes(contents: List<Ranking.Content>) {
        val sakeDetails = contents.map(Ranking.Content::sakeDetail)
        val recommendedSakes = contents.map(RecommendedSakeEntity::of).toSet()

        db.withTransaction {
            sakeDetails.forEach { saveSakeDetail(it) }
            recommendedSakeDao.replaceWith(recommendedSakes)
        }
    }

    private inline fun <reified T, R> combineWithEmpty(
        flows: List<Flow<T>>,
        crossinline transform: suspend (Array<T>) -> R
    ): Flow<R> =
        if (flows.isEmpty()) flow { emit(transform(emptyArray())) }
        else combine(flows, transform)
}
