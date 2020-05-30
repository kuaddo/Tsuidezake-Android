package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkGridBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail

class WantToDrinkGridItem(
    private val drinkDetail: DrinkDetail,
    private val onClickItem: () -> Unit
) : BindableItem<ItemWantToDrinkGridBinding>() {

    override fun initializeViewBinding(view: View): ItemWantToDrinkGridBinding =
        ItemWantToDrinkGridBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_want_to_drink_grid

    override fun bind(viewBinding: ItemWantToDrinkGridBinding, position: Int) {
        viewBinding.drinkDetail = drinkDetail
        viewBinding.root.setOnClickListener { onClickItem() }
        viewBinding.executePendingBindings()
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = 1
}
