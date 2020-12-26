package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = RecommendedSakeEntity.TABLE_NAME,
    primaryKeys = [
        RecommendedSakeEntity.ColumnNames.ORDER,
        RecommendedSakeEntity.ColumnNames.SAKE_ID
    ],
    foreignKeys = [
        ForeignKey(
            entity = SakeEntity::class,
            parentColumns = [SakeEntity.ColumnNames.ID],
            childColumns = [RecommendedSakeEntity.ColumnNames.SAKE_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class RecommendedSakeEntity(
    @ColumnInfo(name = ColumnNames.ORDER)
    val order: Int,
    @ColumnInfo(name = ColumnNames.SAKE_ID)
    val sakeId: Int,
) {
    object ColumnNames {
        const val ORDER = "order"
        const val SAKE_ID = "sake_id"
    }

    companion object {
        const val TABLE_NAME = "recommended_sakes"
    }
}
