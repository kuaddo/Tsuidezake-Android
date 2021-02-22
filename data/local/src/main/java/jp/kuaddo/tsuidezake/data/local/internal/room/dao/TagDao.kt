package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity.Companion.TABLE_NAME

@Dao
internal abstract class TagDao {
    @Transaction
    open suspend fun upsert(tagEntities: Set<TagEntity>) {
        val (updateList, insertList) = tagEntities.partition { hasRow(it.id) }
        insert(insertList)
        update(updateList)
    }

    @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE ${ColumnNames.ID} = :id)")
    protected abstract suspend fun hasRow(id: Int): Boolean

    @Insert
    protected abstract suspend fun insert(tagEntities: List<TagEntity>)

    @Update
    protected abstract suspend fun update(tagEntities: List<TagEntity>)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun findAll(): List<TagEntity>
}
