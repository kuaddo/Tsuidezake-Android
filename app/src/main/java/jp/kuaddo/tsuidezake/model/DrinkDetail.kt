package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// TODO: Use SakeDetail instead of this
@Parcelize
data class DrinkDetail(
    val drink: Drink,
    val price: Int,
    val description: String,
    val tags: List<String> = emptyList() // TODO: 暫定的にString
) : Parcelable
