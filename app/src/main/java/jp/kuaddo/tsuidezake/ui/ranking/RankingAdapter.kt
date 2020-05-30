package jp.kuaddo.tsuidezake.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.kuaddo.tsuidezake.databinding.ItemRankingBinding
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.ui.common.DataBoundListAdapter
import jp.kuaddo.tsuidezake.ui.common.SimpleDiffUtil

class RankingAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: (drink: Drink) -> Unit
) : DataBoundListAdapter<Drink, ItemRankingBinding>(lifecycleOwner, SimpleDiffUtil()) {
    override fun createBinding(parent: ViewGroup): ItemRankingBinding =
        ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemRankingBinding, item: Drink) {
        binding.drink = item
        binding.root.setOnClickListener { onClickItem(item) }
    }
}
