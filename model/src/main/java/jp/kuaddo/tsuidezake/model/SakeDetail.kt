package jp.kuaddo.tsuidezake.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SakeDetail(
    val id: Int,
    val name: String,
    val description: String?,
    val brewer: String?,
    val imageUri: Uri?,
    val tags: List<String>,
    val suitableTemperatures: Set<SuitableTemperature>,
    val goodFoodCategories: Set<FoodCategory>
) : Parcelable
