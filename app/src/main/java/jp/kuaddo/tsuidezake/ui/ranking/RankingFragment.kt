package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentRankingBinding
import jp.kuaddo.tsuidezake.extensions.autoCleared
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.ui.common.AutoScroller

class RankingFragment : DaggerFragment(R.layout.fragment_ranking) {

    private val binding: FragmentRankingBinding by dataBinding()
    private var recommendedAdapter: RecommendedAdapter by autoCleared()
    private var rankingAdapter: RankingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recommendedAdapter = RecommendedAdapter(viewLifecycleOwner) { showSakeDetail() }
        binding.viewPager.let { viewPager ->
            val offset = resources.getDimensionPixelOffset(R.dimen.recommended_item_offset)
            val pageMargin =
                resources.getDimensionPixelOffset(R.dimen.recommended_item_page_margin)

            viewPager.adapter = recommendedAdapter
            viewPager.setPageTransformer { page, position ->
                page.translationX = -position * (2 * offset + pageMargin)
            }

            viewPager.offscreenPageLimit = 3
            viewPager.setUpAutoScroll(
                viewLifecycleOwner,
                AUTO_SCROLL_DURATION,
                recommendedAdapter::getJumpPosition
            )
            viewPager.setCurrentItem(RecommendedAdapter.START_INDEX, false)
        }

        rankingAdapter = RankingAdapter(viewLifecycleOwner) { showSakeDetail() }
        binding.recyclerView.let { recyclerView ->
            recyclerView.adapter = rankingAdapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        // TODO: remove sample data
        recommendedAdapter.submitList(
            (1..7).map { Sake(it, "秘幻 吟醸酒 $it", null) }
        )
        rankingAdapter.submitList(
            (1..20).map { Drink(it, "獺祭$it", "日本酒") }
        )

        showRecommendDrinkDialog()
    }

    private fun showSakeDetail() =
        findNavController().navigate(RankingFragmentDirections.actionRankingToDrinkDetail())

    private fun showRecommendDrinkDialog() =
        findNavController().navigate(RankingFragmentDirections.actionRankingToSwipeSortingDialog())

    companion object {
        private const val AUTO_SCROLL_DURATION = 8000L

        private fun ViewPager2.setUpAutoScroll(
            lifecycleOwner: LifecycleOwner,
            durationMillis: Long,
            getJumpPosition: (position: Int) -> Int?
        ) {
            AutoScroller(lifecycleOwner, durationMillis, this, getJumpPosition)
        }
    }
}
