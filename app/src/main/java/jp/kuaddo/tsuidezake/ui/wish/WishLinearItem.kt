package jp.kuaddo.tsuidezake.ui.wish

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWishLinearBinding
import jp.kuaddo.tsuidezake.model.Sake

data class WishLinearItem(
    private val sake: Sake,
    private val onClickItem: (Sake) -> Unit
) : BindableItem<ItemWishLinearBinding>(sake.id.toLong()) {
    override fun initializeViewBinding(view: View): ItemWishLinearBinding =
        ItemWishLinearBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_wish_linear

    override fun bind(viewBinding: ItemWishLinearBinding, position: Int) {
        viewBinding.name = sake.name
        viewBinding.imageUri = sake.imageUri
        viewBinding.root.setOnClickListener { onClickItem(sake) }
        viewBinding.executePendingBindings()
    }
}
