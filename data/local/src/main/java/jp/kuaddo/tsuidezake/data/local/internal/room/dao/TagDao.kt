package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity

@Dao
internal interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(tagEntities: List<TagEntity>)
}
