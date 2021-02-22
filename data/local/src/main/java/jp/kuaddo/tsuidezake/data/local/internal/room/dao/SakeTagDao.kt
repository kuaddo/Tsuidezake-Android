package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef.Companion.TABLE_NAME

@Dao
internal interface SakeTagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(sakeTagCrossRefs: Set<SakeTagCrossRef>)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll(): List<SakeTagCrossRef>
}
