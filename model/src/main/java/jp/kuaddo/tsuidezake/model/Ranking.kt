package jp.kuaddo.tsuidezake.model

import android.net.Uri

data class Ranking(
    val displayOrder: Int,
    val category: String,
    val contents: List<Content>
) {
    data class Content(
        val rank: Int,
        val sakeId: Int,
        val name: String,
        val imageUri: Uri?,
    )
}