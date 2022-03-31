package jp.kuaddo.tsuidezake.ui.ranking

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.composethemeadapter.MdcTheme
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.ui.common.SimpleDiffUtil

class RankingItemAdapter(
    private val onClickItem: (content: Ranking.Content) -> Unit
) : ListAdapter<Ranking.Content, RankingItemAdapter.RankingItemViewHolder>(SimpleDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingItemViewHolder =
        RankingItemViewHolder(ComposeView(parent.context))

    override fun onBindViewHolder(holder: RankingItemViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onViewRecycled(holder: RankingItemViewHolder) {
        holder.composeView.disposeComposition()
    }

    class RankingItemViewHolder(
        val composeView: ComposeView
    ) : RecyclerView.ViewHolder(composeView) {
        init {
            composeView.setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }

        fun bind(item: Ranking.Content) {
            composeView.setContent {
                MdcTheme {
                    RankingItem(rankingContent = item)
                }
            }
            // TODO: Set click listener
        }
    }
}
