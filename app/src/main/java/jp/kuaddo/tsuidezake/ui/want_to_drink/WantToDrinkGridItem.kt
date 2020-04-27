package jp.kuaddo.tsuidezake.ui.want_to_drink

import com.xwray.groupie.databinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkGridBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail

// TODO: use view binding
class WantToDrinkGridItem(
    private val drinkDetail: DrinkDetail,
    private val onClickItem: () -> Unit
) : BindableItem<ItemWantToDrinkGridBinding>() {

    override fun getLayout(): Int = R.layout.item_want_to_drink_grid

    override fun bind(viewBinding: ItemWantToDrinkGridBinding, position: Int) {
        viewBinding.drinkDetail = drinkDetail
        viewBinding.root.setOnClickListener { onClickItem() }
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = 1
}