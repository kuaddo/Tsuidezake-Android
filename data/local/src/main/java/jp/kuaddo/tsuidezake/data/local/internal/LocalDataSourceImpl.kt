package jp.kuaddo.tsuidezake.data.local.internal

import androidx.room.withTransaction
import jp.kuaddo.tsuidezake.data.local.internal.room.TsuidezakeDB
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeTagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.dao.TagDao
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val db: TsuidezakeDB,
    private val sakeDao: SakeDao,
    private val tagDao: TagDao,
    private val sakeTagDao: SakeTagDao
) : LocalDataSource {
    override fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?> =
        sakeDao.findById(sakeId).map { it?.toUserSake() }.flowOn(Dispatchers.IO)

    override suspend fun saveUserSake(userSake: UserSake) = withContext(Dispatchers.IO) {
        val sakeEntity = SakeEntity.of(userSake)
        val tagEntities = userSake.sakeDetail.tags.map(TagEntity::of)
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(userSake.sakeDetail)

        db.withTransaction {
            sakeDao.upsert(sakeEntity)
            tagDao.upsert(tagEntities)
            sakeTagDao.upsert(sakeTagCrossRefs)
        }
    }

    override suspend fun saveSakeDetail(sakeDetail: SakeDetail) = withContext(Dispatchers.IO) {
        val sakeUpdate = SakeUpdate.of(sakeDetail)
        val tagEntities = sakeDetail.tags.map(TagEntity::of)
        val sakeTagCrossRefs = SakeTagCrossRef.createSakeTagCrossRefs(sakeDetail)

        db.withTransaction {
            sakeDao.upsert(sakeUpdate)
            tagDao.upsert(tagEntities)
            sakeTagDao.upsert(sakeTagCrossRefs)
        }
    }
}
