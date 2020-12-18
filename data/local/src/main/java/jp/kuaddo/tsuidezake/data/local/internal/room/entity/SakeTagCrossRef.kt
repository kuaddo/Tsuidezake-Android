package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames as SakeColumns
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity.ColumnNames as TagColumns

@Entity(
    tableName = SakeTagCrossRef.TABLE_NAME,
    primaryKeys = [
        SakeTagCrossRef.ColumnNames.SAKE_ID,
        SakeTagCrossRef.ColumnNames.TAG_ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = SakeEntity::class,
            parentColumns = [SakeColumns.ID],
            childColumns = [SakeTagCrossRef.ColumnNames.SAKE_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = [TagColumns.ID],
            childColumns = [SakeTagCrossRef.ColumnNames.TAG_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class SakeTagCrossRef(
    @ColumnInfo(name = ColumnNames.SAKE_ID) val sakeId: Int,
    @ColumnInfo(name = ColumnNames.TAG_ID) val tagId: Int
) {
    object ColumnNames {
        const val SAKE_ID = "sake_id"
        const val TAG_ID = "tag_id"
    }

    companion object {
        const val TABLE_NAME = "sake_tag"

        fun createSakeTagCrossRefs(sakeDetail: SakeDetail): List<SakeTagCrossRef> =
            sakeDetail.tags.map { tag -> SakeTagCrossRef(sakeDetail.id, tag.id) }
    }
}
