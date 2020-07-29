package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.View
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
            viewPager.setCurrentItem(RecommendedAdapter.START_INDEX, false)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                private var newPosition: Int? = null

                override fun onPageSelected(position: Int) {
                    newPosition = recommendedAdapter.getJumpPosition(position)
                    if (newPosition != null) {
                        viewPager.isUserInputEnabled = false
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        newPosition?.let { position ->
                            viewPager.setCurrentItem(position, false)
                            newPosition = null
                            viewPager.isUserInputEnabled = true
                        }
                    }
                }
            })
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
}
