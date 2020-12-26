package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
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
        if (hasRow(sakeEntity.id)) updateSakeEntity(sakeEntity) else insertSakeEntity(sakeEntity)

    @Transaction
    open suspend fun upsertSakeUpdate(sakeUpdate: SakeUpdate) =
        if (hasRow(sakeUpdate.id)) updateSakeUpdate(sakeUpdate) else insertSakeUpdate(sakeUpdate)

    @Transaction
    open suspend fun upsertSakeUpdates(sakeUpdates: Set<SakeUpdate>) {
        val (updateList, insertList) = sakeUpdates.partition { hasRow(it.id) }
        insertSakeUpdates(insertList)
        updateSakeUpdates(updateList)
    }

    @Transaction
    open suspend fun upsertWishUpdates(wishUpdates: Set<WishUpdate>) {
        val (updateList, insertList) = wishUpdates.partition { hasRow(it.sakeUpdate.id) }
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
    protected abstract suspend fun insertSakeUpdate(sakeUpdate: SakeUpdate)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateSakeUpdate(sakeUpdate: SakeUpdate)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insertSakeUpdates(sakeUpdates: List<SakeUpdate>)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateSakeUpdates(sakeUpdates: List<SakeUpdate>)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insertWishUpdates(wishUpdates: List<WishUpdate>)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun updateWishUpdates(wishUpdates: List<WishUpdate>)
}
