package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkLinearBinding
import jp.kuaddo.tsuidezake.model.SakeDetail

class WantToDrinkLinearItem(
    private val sakeDetail: SakeDetail,
    private val onClickItem: () -> Unit
) : BindableItem<ItemWantToDrinkLinearBinding>() {

    override fun initializeViewBinding(view: View): ItemWantToDrinkLinearBinding =
        ItemWantToDrinkLinearBinding.bind(view)

    override fun getLayout(): Int = R.layout.item_want_to_drink_linear

    override fun bind(viewBinding: ItemWantToDrinkLinearBinding, position: Int) {
        viewBinding.name = sakeDetail.name
        viewBinding.imageUri = sakeDetail.imageUri
        viewBinding.root.setOnClickListener { onClickItem() }
        viewBinding.executePendingBindings()
    }
}
