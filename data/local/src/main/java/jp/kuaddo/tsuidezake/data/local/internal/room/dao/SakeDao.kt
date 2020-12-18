package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SakeDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id")
    fun findById(id: Int): Flow<RoomSake?>

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id)")
    suspend fun hasRow(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(sakeEntity: SakeEntity)

    @Insert(entity = SakeEntity::class)
    suspend fun insert(sakeUpdate: SakeUpdate)

    @Update(entity = SakeEntity::class)
    suspend fun update(sakeUpdate: SakeUpdate)
}
