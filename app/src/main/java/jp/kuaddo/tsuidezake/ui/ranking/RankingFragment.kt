package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentRankingBinding
import jp.kuaddo.tsuidezake.extensions.autoCleared
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import jp.kuaddo.tsuidezake.ui.common.AutoScroller

@AndroidEntryPoint
class RankingFragment : Fragment(R.layout.fragment_ranking) {
    private val viewModel: RankingViewModel by viewModels()
    private val binding: FragmentRankingBinding by dataBinding()
    private var recommendedAdapter: RecommendedAdapter by autoCleared()
    private var rankingStateAdapter: RankingStateAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recommendedAdapter = RecommendedAdapter(viewLifecycleOwner) { sake ->
            showSakeDetail(sake.id)
        }
        binding.recommendedViewPager.let { viewPager ->
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
        }

        rankingStateAdapter = RankingStateAdapter(childFragmentManager, viewLifecycleOwner)
        binding.rankingViewPager.adapter = rankingStateAdapter

        observe()
    }

    private fun observe() {
        observeViewModelDelegate(viewModel, viewLifecycleOwner)
        viewModel.recommendedSakes.observeNonNull(viewLifecycleOwner) {
            val isInitialLoading = recommendedAdapter.itemCount == 0
            recommendedAdapter.submitList(it)
            if (isInitialLoading) {
                binding.recommendedViewPager.setCurrentItem(RecommendedAdapter.START_INDEX, false)
            }
            // TODO: 表示タイミングを考える
//            showRecommendSakeDialog()
        }
        viewModel.rankings.observeNonNull(viewLifecycleOwner) { rankings ->
            rankingStateAdapter.submitList(rankings)
            TabLayoutMediator(binding.tabLayout, binding.rankingViewPager) { tab, position ->
                tab.text = rankings[position].category
            }.attach()
        }
    }

    private fun showSakeDetail(sakeId: Int) =
        findNavController().navigate(RankingFragmentDirections.actionRankingToSakeDetail(sakeId))

    private fun showRecommendSakeDialog() =
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
