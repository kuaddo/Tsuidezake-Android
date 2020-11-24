package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
        val imageUri: String?
    ) : Parcelable
}
