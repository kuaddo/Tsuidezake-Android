package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import jp.kuaddo.tsuidezake.model.Ranking

@Entity(
    tableName = RankingEntity.TABLE_NAME,
    primaryKeys = [
        RankingEntity.ColumnNames.CATEGORY_ID,
        RankingEntity.ColumnNames.RANK
    ],
    foreignKeys = [
        ForeignKey(
            entity = RankingCategoryEntity::class,
            parentColumns = [RankingCategoryEntity.ColumnNames.ID],
            childColumns = [RankingEntity.ColumnNames.CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SakeEntity::class,
            parentColumns = [SakeEntity.ColumnNames.ID],
            childColumns = [RankingEntity.ColumnNames.SAKE_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class RankingEntity(
    @ColumnInfo(name = ColumnNames.CATEGORY_ID)
    val categoryId: Int,
    @ColumnInfo(name = ColumnNames.RANK)
    val rank: Int,
    @ColumnInfo(name = ColumnNames.SAKE_ID, index = true)
    val sakeId: Int,
) {
    object ColumnNames {
        const val CATEGORY_ID = "category_id"
        const val RANK = "rank"
        const val SAKE_ID = "sake_id"
    }

    companion object {
        const val TABLE_NAME = "rankings"

        fun of(categoryId: Int, content: Ranking.Content) = RankingEntity(
            categoryId = categoryId,
            rank = content.rank,
            sakeId = content.sakeDetail.id
        )
    }
}
