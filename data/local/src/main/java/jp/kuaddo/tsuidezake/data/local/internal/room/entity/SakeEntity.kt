package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.kuaddo.tsuidezake.model.FoodCategory
import jp.kuaddo.tsuidezake.model.SuitableTemperature

@Entity(tableName = SakeEntity.TABLE_NAME)
internal data class SakeEntity(
    @PrimaryKey @ColumnInfo(name = ColumnNames.ID)
    val id: Int,
    @ColumnInfo(name = ColumnNames.NAME)
    val name: String,
    @ColumnInfo(name = ColumnNames.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = ColumnNames.REGION)
    val region: String,
    @ColumnInfo(name = ColumnNames.BREWER)
    val brewer: String?,
    @ColumnInfo(name = ColumnNames.IMAGE_URI)
    val imageUri: String?,
//    @Ignore val tags: List<String>, // TODO: Save tag
    @ColumnInfo(name = ColumnNames.SUITABLE_TEMPERATURES)
    val suitableTemperatures: Set<SuitableTemperature>,
    @ColumnInfo(name = ColumnNames.GOOD_FOOD_CATEGORIES)
    val goodFoodCategories: Set<FoodCategory>,
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
    }
}
