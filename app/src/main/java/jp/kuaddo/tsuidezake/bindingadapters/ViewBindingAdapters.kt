package jp.kuaddo.tsuidezake.bindingadapters

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt


@BindingAdapter("visibleGone")
fun View.bindVisibilityGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleInvisible")
fun View.bindVisibilityInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("isEnabled")
fun View.bindEnabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}

@BindingAdapter("android:layout_marginTop")
fun View.bindMarginTop(topMargin: Float) {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        setMargins(leftMargin, topMargin.roundToInt(), rightMargin, bottomMargin)
    }
}
