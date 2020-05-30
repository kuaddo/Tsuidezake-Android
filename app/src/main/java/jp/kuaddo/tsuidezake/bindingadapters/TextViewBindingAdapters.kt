package jp.kuaddo.tsuidezake.bindingadapters

import android.text.TextUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("isExpanded")
fun TextView.bindIsExpanded(isExpanded: Boolean) {
    if (isExpanded) {
        maxLines = Int.MAX_VALUE
        ellipsize = null
    } else {
        maxLines = 3
        ellipsize = TextUtils.TruncateAt.END
    }
}
