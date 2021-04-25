package jp.kuaddo.tsuidezake.ui.wish

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wada811.databinding.dataBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentWishBinding
import jp.kuaddo.tsuidezake.databinding.ViewEmptyListInstructionBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.ui.MainViewModel
import jp.kuaddo.tsuidezake.util.viewStubDataBinding
import javax.inject.Inject

class WishFragment : DaggerFragment(R.layout.fragment_wish) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wishViewModel: WishViewModel by viewModels { viewModelFactory }
    private val emptyListInstructionViewModel: EmptyListInstructionViewModel by viewModels {
        viewModelFactory
    }
    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val binding: FragmentWishBinding by dataBinding()
    private val emptyInstructionBinding: ViewEmptyListInstructionBinding? by viewStubDataBinding {
        binding.emptyListInstruction
    }
    private val adapter = GroupieAdapter().apply { spanCount = 2 }
    private val recommendedGridAdapter = GroupieAdapter().apply { spanCount = 2 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wishViewModel = wishViewModel
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener { wishViewModel.refresh() }

        observe()
    }

    private fun observe() {
        observeViewModelDelegate(wishViewModel, viewLifecycleOwner)
        observeViewModelDelegate(emptyListInstructionViewModel, viewLifecycleOwner)

        wishViewModel.groupedWishListWithMode
            .observeNonNull(viewLifecycleOwner) { (groupedWishList, isGrid) ->
                if (groupedWishList.isEmpty()) inflateEmptyListInstruction()
                adapter.update(getGroups(groupedWishList, isGrid))
            }
        wishViewModel.isGridMode.observeNonNull(viewLifecycleOwner) { isGrid ->
            binding.recyclerView.layoutManager = getLayoutManager(isGrid)
        }
        emptyListInstructionViewModel.recommendedSakes
            .observeNonNull(viewLifecycleOwner) { recommendedSakes ->
                val items = recommendedSakes.map { WishGridItem(it, ::showSakeDetailFragment) }
                recommendedGridAdapter.update(items)
            }
    }

    private fun getLayoutManager(isGrid: Boolean) = if (isGrid) {
        GridLayoutManager(requireContext(), adapter.spanCount).apply {
            spanSizeLookup = adapter.spanSizeLookup
        }
    } else {
        LinearLayoutManager(requireContext())
    }

    private fun getGroups(groupedWishList: GroupedWishList, isGrid: Boolean) =
        groupedWishList.toSortedMap()
            .map { (region, sakeList) ->
                Section().apply {
                    setHeader(WishHeaderItem(region))
                    addAll(sakeList.map { getWishGridItem(Sake.of(it), isGrid) })
                }
            }

    private fun getWishGridItem(
        sake: Sake,
        isGrid: Boolean
    ): BindableItem<out ViewDataBinding> {
        return if (isGrid) {
            WishGridItem(sake, ::showSakeDetailFragment)
        } else {
            WishLinearItem(sake, ::showSakeDetailFragment)
        }
    }

    private fun inflateEmptyListInstruction() {
        if (emptyInstructionBinding != null) return
        binding.emptyListInstruction.viewStub?.inflate()

        emptyInstructionBinding?.let { instructionBinding ->
            instructionBinding.recommendedGrid.adapter = recommendedGridAdapter
            instructionBinding.moveToRankingButton.setOnClickListener {
                moveToRankingFragment()
            }
        }
        emptyListInstructionViewModel.loadRecommendedSakes()
    }

    private fun showSakeDetailFragment(sake: Sake) = findNavController().navigate(
        WishFragmentDirections.actionWishToSakeDetail(sake.id)
    )

    private fun moveToRankingFragment() = mainViewModel.showRanking()
}
