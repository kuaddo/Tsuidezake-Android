package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.kuaddo.tsuidezake.databinding.ItemWantToDrinkBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail
import jp.kuaddo.tsuidezake.ui.common.DataBoundListAdapter
import jp.kuaddo.tsuidezake.ui.common.SimpleDiffUtil

class WantToDrinkAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: (drinkDetail: DrinkDetail) -> Unit
) : DataBoundListAdapter<DrinkDetail, ItemWantToDrinkBinding>(lifecycleOwner, SimpleDiffUtil()) {
    override fun createBinding(parent: ViewGroup): ItemWantToDrinkBinding =
        ItemWantToDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemWantToDrinkBinding, item: DrinkDetail) {
        binding.drinkDetail = item
        binding.root.setOnClickListener { onClickItem(item) }
    }
}