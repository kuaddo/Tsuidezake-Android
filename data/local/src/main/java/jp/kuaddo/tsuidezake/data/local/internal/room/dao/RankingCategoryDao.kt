package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomRanking
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class RankingCategoryDao {
    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun findAll(): Flow<List<RoomRanking>>

    @Transaction
    open suspend fun replaceWith(rankingCategories: Set<RankingCategoryEntity>) {
        deleteAll()
        insert(rankingCategories.toList())
    }

    @Insert
    protected abstract suspend fun insert(rankingCategories: List<RankingCategoryEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    protected abstract suspend fun deleteAll()
}
