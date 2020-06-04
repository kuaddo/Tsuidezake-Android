package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import jp.kuaddo.tsuidezake.type.FoodCategory
import jp.kuaddo.tsuidezake.type.SuitableTemparature
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SakeDetail(
    val id: Int,
    val name: String,
    val description: String?,
    val brewer: String?,
    val imageUrl: String?,
    val tags: List<String>,
    // TODO: マルチモジュール化後にSuitableTemperatureとFoodCategoryに対する依存を削除する
    val suitableTemperatures: List<SuitableTemparature>,
    val goodFoodCategories: List<FoodCategory>
) : Parcelable