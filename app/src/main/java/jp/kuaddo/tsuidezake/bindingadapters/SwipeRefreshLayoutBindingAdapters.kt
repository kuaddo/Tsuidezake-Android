package jp.kuaddo.tsuidezake.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.bindIsRefreshing(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}
