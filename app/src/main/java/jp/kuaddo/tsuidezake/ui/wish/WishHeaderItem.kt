package jp.kuaddo.tsuidezake.ui.wish

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWishHeaderBinding

data class WishHeaderItem(
    private val areaName: String
) : BindableItem<ItemWishHeaderBinding>(areaName.hashCode().toLong()) {
    override fun initializeViewBinding(view: View): ItemWishHeaderBinding =
        ItemWishHeaderBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_wish_header

    override fun bind(viewBinding: ItemWishHeaderBinding, position: Int) {
        viewBinding.areaName = areaName
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount
}
