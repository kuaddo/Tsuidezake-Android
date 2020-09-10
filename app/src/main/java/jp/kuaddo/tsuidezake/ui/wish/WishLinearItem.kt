package jp.kuaddo.tsuidezake.ui.wish

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWishLinearBinding
import jp.kuaddo.tsuidezake.model.SakeDetail

data class WishLinearItem(
    private val sakeDetail: SakeDetail,
    private val onClickItem: (SakeDetail) -> Unit
) : BindableItem<ItemWishLinearBinding>(sakeDetail.id.toLong()) {
    override fun initializeViewBinding(view: View): ItemWishLinearBinding =
        ItemWishLinearBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_wish_linear

    override fun bind(viewBinding: ItemWishLinearBinding, position: Int) {
        viewBinding.name = sakeDetail.name
        viewBinding.imageUri = sakeDetail.imageUri
        viewBinding.root.setOnClickListener { onClickItem(sakeDetail) }
        viewBinding.executePendingBindings()
    }
}
