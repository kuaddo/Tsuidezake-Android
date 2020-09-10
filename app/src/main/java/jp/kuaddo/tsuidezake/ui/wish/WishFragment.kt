package jp.kuaddo.tsuidezake.ui.wish

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wada811.databinding.dataBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentWishBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import jp.kuaddo.tsuidezake.model.SakeDetail
import javax.inject.Inject

class WishFragment : DaggerFragment(R.layout.fragment_wish) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wishViewModel: WishViewModel by viewModels { viewModelFactory }
    private val binding: FragmentWishBinding by dataBinding()
    private val adapter = GroupAdapter<GroupieViewHolder>().apply { spanCount = 2 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wishViewModel = wishViewModel
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener { wishViewModel.refresh() }

        observe()
    }

    private fun observe() {
        observeViewModelDelegate(wishViewModel, viewLifecycleOwner)
        wishViewModel.groupedWishListWithMode
            .observeNonNull(viewLifecycleOwner) { (groupedWishList, isGrid) ->
                adapter.update(getGroups(groupedWishList, isGrid))
            }
        wishViewModel.isGridMode.observeNonNull(viewLifecycleOwner) { isGrid ->
            binding.recyclerView.layoutManager = getLayoutManager(isGrid)
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
                    addAll(sakeList.map { getWishGridItem(it, isGrid) })
                }
            }

    private fun getWishGridItem(
        sakeDetail: SakeDetail,
        isGrid: Boolean
    ): BindableItem<out ViewDataBinding> {
        return if (isGrid) {
            WishGridItem(sakeDetail, ::showSakeDetailFragment)
        } else {
            WishLinearItem(sakeDetail, ::showSakeDetailFragment)
        }
    }

    private fun showSakeDetailFragment(sakeDetail: SakeDetail) = findNavController().navigate(
        WishFragmentDirections.actionWishToSakeDetail(sakeDetail.id)
    )
}
