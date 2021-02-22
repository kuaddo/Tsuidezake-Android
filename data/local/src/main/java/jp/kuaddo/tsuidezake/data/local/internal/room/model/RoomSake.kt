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
        sakeDetail = toSakeDetail(),
        isAddedToWish = sakeEntity.isAddedToWish,
        isAddedToTasted = sakeEntity.isAddedToTasted
    )

    fun toSakeDetail(): SakeDetail {
        val sakeInfo = sakeEntity.sakeInfo
        return SakeDetail(
            id = sakeInfo.id,
            name = sakeInfo.name,
            description = sakeInfo.description,
            region = sakeInfo.region,
            brewer = sakeInfo.brewer,
            imageUri = sakeInfo.imageUri,
            tags = tagEntities.map { it.toTag() },
            suitableTemperatures = sakeInfo.suitableTemperatures,
            goodFoodCategories = sakeInfo.goodFoodCategories
        )
    }
}
