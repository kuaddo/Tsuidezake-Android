package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SakeDetail(
    val id: Int,
    val name: String,
    val description: String?,
    val region: String,
    val brewer: String?,
    val imageUri: String?,
    val tags: List<Tag>,
    val suitableTemperatures: Set<SuitableTemperature>,
    val goodFoodCategories: Set<FoodCategory>
) : Parcelable
