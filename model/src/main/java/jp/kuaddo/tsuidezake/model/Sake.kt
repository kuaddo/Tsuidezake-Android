package jp.kuaddo.tsuidezake.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sake(
    val id: Int,
    val name: String,
    val imageUri: String?
) : Parcelable {
    companion object {
        fun of(sakeDetail: SakeDetail) = Sake(
            id = sakeDetail.id,
            name = sakeDetail.name,
            imageUri = sakeDetail.imageUri
        )
    }
}
