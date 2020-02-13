package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Drink(
    // TODO: add image URL
    val rank: Int,
    val name: String,
    val category: String // TODO: create enum
) : Parcelable