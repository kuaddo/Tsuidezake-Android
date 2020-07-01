package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SakeDetail(
    val id: Int,
    val name: String,
    val description: String?,
    val brewer: String?,
    val imageUrl: String?,
    val tags: List<String>,
    val suitableTemperatures: Set<SuitableTemperature>,
    val goodFoodCategories: List<FoodCategory>
) : Parcelable
