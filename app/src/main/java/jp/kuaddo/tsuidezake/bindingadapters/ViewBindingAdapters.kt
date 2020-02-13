package jp.kuaddo.tsuidezake.bindingadapters

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun View.bindVisibilityGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleInvisible")
fun View.bindVisibilityInvisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("rippleBackground", "rippleColor", requireAll = false)
fun View.bindRipple(backgroundColor: Int, rippleColor: Int?) {
    val id = TypedValue().let {
        context.theme.resolveAttribute(android.R.attr.colorControlHighlight, it, true)
        it.resourceId
    }
    val defaultRippleColor = ResourcesCompat.getColor(resources, id, null)
    background = RippleDrawable(
        ColorStateList.valueOf(rippleColor ?: defaultRippleColor),
        ColorDrawable(backgroundColor),
        null
    ).mutate()
}

@BindingAdapter("rippleBackground", "rippleColor", requireAll = false)
fun View.bindRipple(backgroundDrawable: Drawable, rippleColor: Int?) {
    val id = TypedValue().let {
        context.theme.resolveAttribute(android.R.attr.colorControlHighlight, it, true)
        it.resourceId
    }
    val defaultRippleColor = ResourcesCompat.getColor(resources, id, null)
    background = RippleDrawable(
        ColorStateList.valueOf(rippleColor ?: defaultRippleColor),
        backgroundDrawable,
        null
    ).mutate()
}