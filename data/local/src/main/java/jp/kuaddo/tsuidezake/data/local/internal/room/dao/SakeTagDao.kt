package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef

@Dao
internal interface SakeTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(sakeTagCrossRefs: List<SakeTagCrossRef>)
}
