package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuitableTemperature
import jp.kuaddo.tsuidezake.model.UserSake

@Entity(
    tableName = SakeEntity.TABLE_NAME,
    primaryKeys = [SakeEntity.ColumnNames.ID]
)
internal data class SakeEntity(
    @Embedded
    val sakeInfo: SakeInfo,
    @ColumnInfo(name = ColumnNames.IS_ADDED_TO_WISH, defaultValue = "false")
    val isAddedToWish: Boolean,
    @ColumnInfo(name = ColumnNames.IS_ADDED_TO_TASTED, defaultValue = "false")
    val isAddedToTasted: Boolean
) {
    object ColumnNames {
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val REGION = "region"
        const val BREWER = "brewer"
        const val IMAGE_URI = "image_uri"
        const val SUITABLE_TEMPERATURES = "suitable_temperatures"
        const val GOOD_FOOD_CATEGORIES = "good_food_categories"
        const val IS_ADDED_TO_WISH = "is_added_to_wish"
        const val IS_ADDED_TO_TASTED = "is_added_to_tasted"
    }

    companion object {
        const val TABLE_NAME = "sakes"

        fun of(userSake: UserSake) = SakeEntity(
            sakeInfo = SakeInfo.of(userSake.sakeDetail),
            isAddedToWish = userSake.isAddedToWish,
            isAddedToTasted = userSake.isAddedToTasted
        )
    }
}

internal data class SakeInfo(
    @ColumnInfo(name = SakeEntity.ColumnNames.ID)
    val id: Int,
    @ColumnInfo(name = SakeEntity.ColumnNames.NAME)
    val name: String,
    @ColumnInfo(name = SakeEntity.ColumnNames.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = SakeEntity.ColumnNames.REGION)
    val region: String,
    @ColumnInfo(name = SakeEntity.ColumnNames.BREWER)
    val brewer: String?,
    @ColumnInfo(name = SakeEntity.ColumnNames.IMAGE_URI)
    val imageUri: String?,
    @ColumnInfo(name = SakeEntity.ColumnNames.SUITABLE_TEMPERATURES)
    val suitableTemperatures: Set<SuitableTemperature>,
    @ColumnInfo(name = SakeEntity.ColumnNames.GOOD_FOOD_CATEGORIES)
    val goodFoodCategories: Set<FoodCategory>
) {
    companion object {
        fun of(sakeDetail: SakeDetail): SakeInfo = SakeInfo(
            id = sakeDetail.id,
            name = sakeDetail.name,
            description = sakeDetail.description,
            region = sakeDetail.region,
            brewer = sakeDetail.brewer,
            imageUri = sakeDetail.imageUri,
            suitableTemperatures = sakeDetail.suitableTemperatures,
            goodFoodCategories = sakeDetail.goodFoodCategories
        )
    }
}

internal data class WishUpdate(
    @Embedded
    val sakeInfo: SakeInfo,
    @ColumnInfo(name = SakeEntity.ColumnNames.IS_ADDED_TO_WISH)
    val isAddedToWish: Boolean
) {
    companion object {
        fun of(sakeDetail: SakeDetail, isAddedToWish: Boolean): WishUpdate = WishUpdate(
            sakeInfo = SakeInfo.of(sakeDetail),
            isAddedToWish = isAddedToWish
        )
    }
}
