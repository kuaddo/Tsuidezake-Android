package jp.kuaddo.tsuidezake.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sake(
    val id: Int,
    val name: String,
    val imageUri: Uri?
) : Parcelable {
    companion object
}
