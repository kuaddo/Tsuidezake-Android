package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeUpdate
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SakeDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id")
    fun findById(id: Int): Flow<SakeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(sakeEntity: SakeEntity)

    @Insert(entity = SakeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun upsert(sakeUpdate: SakeUpdate)
}