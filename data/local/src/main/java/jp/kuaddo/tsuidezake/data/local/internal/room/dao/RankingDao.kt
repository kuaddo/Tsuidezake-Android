package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity.Companion.TABLE_NAME

@Dao
internal abstract class RankingDao {
    @Transaction
    open suspend fun replaceWith(rankings: Set<RankingEntity>) {
        deleteAll()
        insert(rankings.toList())
    }

    @Insert
    protected abstract suspend fun insert(rankings: List<RankingEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    protected abstract suspend fun deleteAll()
}
