package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import jp.kuaddo.tsuidezake.model.Ranking

@Entity(
    tableName = RecommendedSakeEntity.TABLE_NAME,
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
    @PrimaryKey
    @ColumnInfo(name = ColumnNames.ORDER)
    val order: Int,
    @ColumnInfo(name = ColumnNames.SAKE_ID, index = true)
    val sakeId: Int,
) {
    object ColumnNames {
        const val ORDER = "order"
        const val SAKE_ID = "sake_id"
    }

    companion object {
        const val TABLE_NAME = "recommended_sakes"

        fun of(content: Ranking.Content) = RecommendedSakeEntity(
            order = content.rank,
            sakeId = content.sakeDetail.id
        )
    }
}
