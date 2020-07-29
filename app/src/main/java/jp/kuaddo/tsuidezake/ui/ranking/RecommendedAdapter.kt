package jp.kuaddo.tsuidezake.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import jp.kuaddo.tsuidezake.databinding.ItemRecommendedBinding
import jp.kuaddo.tsuidezake.extensions.diffUtil
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.ui.common.DataBoundListAdapter

class RecommendedAdapter(
    lifecycleOwner: LifecycleOwner,
    private val onClickItem: (sake: Sake) -> Unit
) : DataBoundListAdapter<Sake, ItemRecommendedBinding>(lifecycleOwner, Sake.diffUtil) {
    override fun createBinding(parent: ViewGroup): ItemRecommendedBinding =
        ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemRecommendedBinding, item: Sake) {
        binding.sakeName = item.name
        binding.root.setOnClickListener { onClickItem(item) }
    }
}
