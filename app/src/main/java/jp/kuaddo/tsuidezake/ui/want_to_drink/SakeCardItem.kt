package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemSakeCardBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail

class SakeCardItem(
    private val drinkDetail: DrinkDetail,
    private val onClickItem: () -> Unit
) : BindableItem<ItemSakeCardBinding>() {

    override fun initializeViewBinding(view: View): ItemSakeCardBinding =
        ItemSakeCardBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_sake_card

    override fun bind(viewBinding: ItemSakeCardBinding, position: Int) {
        viewBinding.sakeName = drinkDetail.drink.name
        viewBinding.root.setOnClickListener { onClickItem() }
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = 1
}
