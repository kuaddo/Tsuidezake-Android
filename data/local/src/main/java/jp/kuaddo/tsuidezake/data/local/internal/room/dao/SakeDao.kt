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
    open suspend fun upsert(sakeEntity: SakeEntity) =
        if (hasRow(sakeEntity.id)) update(sakeEntity) else insert(sakeEntity)

    @Transaction
    open suspend fun upsert(sakeUpdate: SakeUpdate) =
        if (hasRow(sakeUpdate.id)) update(sakeUpdate) else insert(sakeUpdate)

    @Transaction
    open suspend fun upsert(wishUpdates: Set<WishUpdate>) {
        val (updateList, insertList) = wishUpdates.partition { hasRow(it.sakeUpdate.id) }
        insert(insertList)
        update(updateList)
    }

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id)")
    protected abstract suspend fun hasRow(id: Int): Boolean

    @Insert
    protected abstract suspend fun insert(sakeEntity: SakeEntity)

    @Update
    protected abstract suspend fun update(sakeEntity: SakeEntity)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insert(sakeUpdate: SakeUpdate)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun update(sakeUpdate: SakeUpdate)

    @Insert(entity = SakeEntity::class)
    protected abstract suspend fun insert(wishUpdates: List<WishUpdate>)

    @Update(entity = SakeEntity::class)
    protected abstract suspend fun update(wishUpdates: List<WishUpdate>)
}
