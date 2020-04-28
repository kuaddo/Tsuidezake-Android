package jp.kuaddo.tsuidezake.ui.want_to_drink

import com.xwray.groupie.databinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkHeaderBinding

// TODO: use view binding
class WantToDrinkHeaderItem(
    private val areaName: String
) : BindableItem<ItemWantToDrinkHeaderBinding>() {

    override fun getLayout(): Int = R.layout.item_want_to_drink_header

    override fun bind(viewBinding: ItemWantToDrinkHeaderBinding, position: Int) {
        viewBinding.areaName = areaName
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount
}