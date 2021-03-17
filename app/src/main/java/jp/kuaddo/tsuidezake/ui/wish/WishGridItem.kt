package jp.kuaddo.tsuidezake.ui.wish

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemSakeCardBinding
import jp.kuaddo.tsuidezake.model.Sake

data class WishGridItem(
    private val sake: Sake,
    private val onClickItem: (Sake) -> Unit
) : BindableItem<ItemSakeCardBinding>(sake.id.toLong()) {
    override fun initializeViewBinding(view: View): ItemSakeCardBinding =
        ItemSakeCardBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_sake_card

    override fun bind(viewBinding: ItemSakeCardBinding, position: Int) {
        viewBinding.name = sake.name
        viewBinding.imageUri = sake.imageUri
        viewBinding.root.setOnClickListener { onClickItem(sake) }
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = 1
}
