package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkHeaderBinding

class WantToDrinkHeaderItem(
    private val areaName: String
) : BindableItem<ItemWantToDrinkHeaderBinding>() {

    override fun initializeViewBinding(view: View): ItemWantToDrinkHeaderBinding =
        ItemWantToDrinkHeaderBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_want_to_drink_header

    override fun bind(viewBinding: ItemWantToDrinkHeaderBinding, position: Int) {
        viewBinding.areaName = areaName
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount
}
