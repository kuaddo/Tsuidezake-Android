package jp.kuaddo.tsuidezake.data.local.internal.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef.ColumnNames
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef.Companion.TABLE_NAME

@Dao
internal abstract class SakeTagDao {
    @Transaction
    open suspend fun upsert(sakeTagCrossRefs: List<SakeTagCrossRef>) {
        val (updateList, insertList) = sakeTagCrossRefs.partition {
            hasRow(sakeId = it.sakeId, tagId = it.tagId)
        }
        insert(insertList)
        update(updateList)
    }

    @Query(
        """
        SELECT EXISTS(
            SELECT * FROM $TABLE_NAME 
            WHERE ${ColumnNames.SAKE_ID} = :sakeId AND ${ColumnNames.TAG_ID} = :tagId 
        )
        """
    )
    protected abstract suspend fun hasRow(sakeId: Int, tagId: Int): Boolean

    @Insert
    protected abstract suspend fun insert(sakeTagCrossRefs: List<SakeTagCrossRef>)

    @Update
    protected abstract suspend fun update(sakeTagCrossRefs: List<SakeTagCrossRef>)
}
