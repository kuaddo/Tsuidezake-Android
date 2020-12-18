package jp.kuaddo.tsuidezake.data.local.internal.room.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.UserSake
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity.ColumnNames as SakeColumns
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeTagCrossRef.ColumnNames as SakeTagColumns
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.TagEntity.ColumnNames as TagColumns

internal data class RoomSake(
    @Embedded val sakeEntity: SakeEntity,
    @Relation(
        parentColumn = SakeColumns.ID,
        entityColumn = TagColumns.ID,
        associateBy = Junction(
            SakeTagCrossRef::class,
            parentColumn = SakeTagColumns.SAKE_ID,
            entityColumn = SakeTagColumns.TAG_ID
        )
    )
    val tagEntities: List<TagEntity>
) {
    fun toUserSake() = UserSake(
        sakeDetail = SakeDetail(
            id = sakeEntity.id,
            name = sakeEntity.name,
            description = sakeEntity.description,
            region = sakeEntity.region,
            brewer = sakeEntity.brewer,
            imageUri = sakeEntity.imageUri,
            tags = tagEntities.map { it.toTag() },
            suitableTemperatures = sakeEntity.suitableTemperatures,
            goodFoodCategories = sakeEntity.goodFoodCategories
        ),
        isAddedToWish = sakeEntity.isAddedToWish,
        isAddedToTasted = sakeEntity.isAddedToTasted
    )
}
