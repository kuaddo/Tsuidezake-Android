package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = RankingCategoryEntity.TABLE_NAME,
    indices = [
        Index(
            value = [RankingCategoryEntity.ColumnNames.NAME],
            unique = true
        ),
        Index(
            value = [
                RankingCategoryEntity.ColumnNames.NAME,
                RankingCategoryEntity.ColumnNames.ORDER
            ],
            unique = true
        )
    ]
)
internal data class RankingCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ColumnNames.ID)
    val id: Long,
    @ColumnInfo(name = ColumnNames.NAME)
    val name: String,
    @ColumnInfo(name = ColumnNames.ORDER)
    val order: Int
) {
    object ColumnNames {
        const val ID = "id"
        const val NAME = "name"
        const val ORDER = "order"
    }

    companion object {
        const val TABLE_NAME = "ranking_categories"
    }
}
