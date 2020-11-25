package jp.kuaddo.tsuidezake.data.local.internal

import jp.kuaddo.tsuidezake.data.local.internal.room.dao.SakeDao
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val sakeDao: SakeDao
) : LocalDataSource {
    override fun loadUserSakeFlow(sakeId: Int): Flow<UserSake?> =
        sakeDao.findById(sakeId).map { it?.toUserSake() }

    override suspend fun saveUserSake(userSake: UserSake) =
        sakeDao.upsert(SakeEntity.of(userSake))

    override suspend fun saveSakeDetail(sakeDetail: SakeDetail) =
        sakeDao.upsert(SakeUpdate.of(sakeDetail))
}
