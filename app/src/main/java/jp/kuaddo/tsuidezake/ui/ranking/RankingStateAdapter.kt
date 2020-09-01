package jp.kuaddo.tsuidezake.ui.ranking

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import jp.kuaddo.tsuidezake.model.ObsoleteRanking

class RankingStateAdapter(
    fragmentManager: FragmentManager,
    lifecycleOwner: LifecycleOwner
) : FragmentStateAdapter(fragmentManager, lifecycleOwner.lifecycle) {
    private val rankings = mutableListOf<ObsoleteRanking>()

    override fun getItemCount(): Int = rankings.size

    override fun createFragment(position: Int): Fragment =
        RankingItemFragment.newInstance(rankings[position].sakes)

    fun submitList(rankings: List<ObsoleteRanking>) {
        this.rankings.clear()
        this.rankings.addAll(rankings)
    }
}
