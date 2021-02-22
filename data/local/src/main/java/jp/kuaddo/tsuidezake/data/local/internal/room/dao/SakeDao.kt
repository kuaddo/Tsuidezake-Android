package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeInfo
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.WishUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class SakeDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.IS_ADDED_TO_WISH} = 1")
    abstract fun selectWishList(): Flow<List<RoomSake>>

    @Query("SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id")
    abstract fun findById(id: Int): Flow<RoomSake?>

    @Transaction
    open suspend fun upsertSakeEntity(sakeEntity: SakeEntity) =
        if (hasRow(sakeEntity.sakeInfo.id)) updateSakeEntity(sakeEntity)
        else insertSakeEntity(sakeEntity)

    @Transaction
    open suspend fun upsertSakeInfo(sakeInfo: SakeInfo) =
        if (hasRow(sakeInfo.id)) updateSakeInfo(sakeInfo) else insertSakeInfo(sakeInfo)

    @Transaction
    open suspend fun upsertSakeInfos(sakeInfos: Set<SakeInfo>) {
        val (updateList, insertList) = sakeInfos.partition { hasRow(it.id) }
        insertSakeInfos(insertList)
        updateSakeInfos(updateList)
    }

    @Transaction
    open suspend fun upsertWishUpdates(wishUpdates: Set<WishUpdate>) {
        val (updateList, insertList) = wishUpdates.partition { hasRow(it.sakeInfo.id) }
        insertWishUpdates(insertList)
        updateWishUpdates(updateList)
    }

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id)")
    protected abstract suspend fun hasRow(id: Int): Boolean

    @Insert
    protected abstract suspend fun insertSakeEntity(sakeEntity: SakeEntity)

    @Update
    protected abstract suspend fun updateSakeEntity(sakeEntity: SakeEntity)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insertSakeInfo(sakeInfo: SakeInfo)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateSakeInfo(sakeInfo: SakeInfo)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insertSakeInfos(sakeInfos: List<SakeInfo>)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateSakeInfos(sakeInfos: List<SakeInfo>)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insertWishUpdates(wishUpdates: List<WishUpdate>)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateWishUpdates(wishUpdates: List<WishUpdate>)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun selectAll(): List<RoomSake>
}
