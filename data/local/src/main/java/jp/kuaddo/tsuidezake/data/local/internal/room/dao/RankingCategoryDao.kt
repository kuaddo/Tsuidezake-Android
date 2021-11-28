package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity.Companion.TABLE_NAME
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomRanking
import kotlinx.coroutines.flow.Flow
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity.ColumnNames as RankingCategoryColumns

@Dao
internal abstract class RankingCategoryDao {
    @Transaction
    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun findAll(): Flow<List<RoomRanking>>

    @Query(
        """
            SELECT ${RankingCategoryColumns.ID} FROM $TABLE_NAME 
            WHERE ${RankingCategoryColumns.NAME} = :name
        """
    )
    abstract suspend fun selectIdBy(name: String): Int

    @Transaction
    open suspend fun replaceWith(rankingCategories: Set<RankingCategoryEntity>) {
        deleteAll()
        insert(rankingCategories.toList())
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    @Insert
    abstract suspend fun insert(rankingCategories: List<RankingCategoryEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    protected abstract suspend fun deleteAll()

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun selectAll(): List<RankingCategoryEntity>
}
