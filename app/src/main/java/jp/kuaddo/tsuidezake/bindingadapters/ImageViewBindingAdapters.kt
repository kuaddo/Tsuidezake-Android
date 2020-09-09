package jp.kuaddo.tsuidezake.bindingadapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import jp.kuaddo.tsuidezake.util.GlideApp

@BindingAdapter("uri")
fun ImageView.bindImageUri(uri: Uri?) {
    if (uri == null) setImageDrawable(null)
    else GlideApp.with(context).load(uri).into(this)
}

@Suppress("unused")
@BindingMethods(
    BindingMethod(
        type = ImageView::class,
        attribute = "app:tint",
        method = "setImageTintList"
    )
)
private object ImageViewBindingAdapters
