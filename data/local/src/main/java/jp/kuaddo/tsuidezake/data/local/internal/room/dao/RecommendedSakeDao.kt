package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity.Companion.TABLE_NAME

@Dao
internal abstract class RecommendedSakeDao {
    @Transaction
    open suspend fun replaceWith(recommendedSakes: List<RecommendedSakeEntity>) {
        deleteAll()
        insert(recommendedSakes)
    }

    @Insert
    protected abstract suspend fun insert(recommendedSakes: List<RecommendedSakeEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    protected abstract suspend fun deleteAll()
}
