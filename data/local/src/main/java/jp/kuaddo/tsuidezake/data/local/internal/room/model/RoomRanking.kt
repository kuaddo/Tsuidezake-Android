package jp.kuaddo.tsuidezake.data.local.internal.room.model

import androidx.room.Embedded
import androidx.room.Relation
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingCategoryEntity.ColumnNames as RankingCategoryColumns
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.RankingEntity.ColumnNames as RankingColumns

internal data class RoomRanking(
    @Embedded val rankingCategoryEntity: RankingCategoryEntity,
    @Relation(
        parentColumn = RankingCategoryColumns.ID,
        entityColumn = RankingColumns.CATEGORY_ID
    )
    val rankingEntities: List<RankingEntity>
)
