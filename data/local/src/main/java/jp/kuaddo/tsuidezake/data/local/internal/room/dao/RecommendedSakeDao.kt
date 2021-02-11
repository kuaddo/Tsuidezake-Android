package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RecommendedSakeEntity.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class RecommendedSakeDao {
    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun findAll(): Flow<List<RecommendedSakeEntity>>

    @Transaction
    open suspend fun replaceWith(recommendedSakes: Set<RecommendedSakeEntity>) {
        deleteAll()
        insert(recommendedSakes.toList())
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Insert
    abstract suspend fun insert(recommendedSakes: List<RecommendedSakeEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    protected abstract suspend fun deleteAll()
}
