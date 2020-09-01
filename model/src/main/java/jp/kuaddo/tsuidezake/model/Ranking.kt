package jp.kuaddo.tsuidezake.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Ranking(
    val displayOrder: Int,
    val category: String,
    val contents: List<Content>
) {
    @Parcelize
    data class Content(
        val rank: Int,
        val sakeId: Int,
        val name: String,
        val imageUri: Uri?,
    ) : Parcelable
}