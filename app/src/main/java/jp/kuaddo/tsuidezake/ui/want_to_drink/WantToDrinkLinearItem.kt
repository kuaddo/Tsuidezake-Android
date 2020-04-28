package jp.kuaddo.tsuidezake.ui.want_to_drink

import com.xwray.groupie.databinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkLinearBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail

// TODO: use view binding
class WantToDrinkLinearItem(
    private val drinkDetail: DrinkDetail,
    private val onClickItem: () -> Unit
) : BindableItem<ItemWantToDrinkLinearBinding>() {

    override fun getLayout(): Int = R.layout.item_want_to_drink_linear

    override fun bind(viewBinding: ItemWantToDrinkLinearBinding, position: Int) {
        viewBinding.drinkDetail = drinkDetail
        viewBinding.root.setOnClickListener { onClickItem() }
    }
}