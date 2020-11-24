package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tag(
    val id: Int,
    val name: String
) : Parcelable
