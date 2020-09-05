package jp.kuaddo.tsuidezake.bindingadapters

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import jp.kuaddo.tsuidezake.util.GlideApp

@BindingAdapter("url")
fun ImageView.bindImageUrl(url: String?) =
    url?.let { GlideApp.with(context).load(it).into(this) }

@BindingAdapter("uri")
fun ImageView.bindImageUrl(uri: Uri?) =
    uri?.let { GlideApp.with(context).load(it).into(this) }

@BindingAdapter("bitmap")
fun ImageView.bindImageBitmap(bitmap: Bitmap?) =
    bitmap?.let { setImageBitmap(bitmap) }

@Suppress("unused")
@BindingMethods(
    BindingMethod(
        type = ImageView::class,
        attribute = "app:tint",
        method = "setImageTintList"
    )
)
private object ImageViewBindingAdapters
