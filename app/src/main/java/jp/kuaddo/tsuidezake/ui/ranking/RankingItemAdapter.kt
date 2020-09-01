package jp.kuaddo.tsuidezake.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.kuaddo.tsuidezake.databinding.ItemRankingBinding
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.ui.common.DataBoundListAdapter
import jp.kuaddo.tsuidezake.ui.common.SimpleDiffUtil

class RankingItemAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: (content: Ranking.Content) -> Unit
) : DataBoundListAdapter<Ranking.Content, ItemRankingBinding>(lifecycleOwner, SimpleDiffUtil()) {
    override fun createBinding(parent: ViewGroup): ItemRankingBinding =
        ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemRankingBinding, item: Ranking.Content, position: Int) {
        binding.content = item
        binding.root.setOnClickListener { onClickItem(item) }
    }
}
