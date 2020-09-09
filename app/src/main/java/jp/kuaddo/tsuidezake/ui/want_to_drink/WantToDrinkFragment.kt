package jp.kuaddo.tsuidezake.ui.want_to_drink

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
import jp.kuaddo.tsuidezake.databinding.FragmentWantToDrinkBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import jp.kuaddo.tsuidezake.model.SakeDetail
import javax.inject.Inject

class WantToDrinkFragment : DaggerFragment(R.layout.fragment_want_to_drink) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wantToDrinkViewModel: WantToDrinkViewModel by viewModels { viewModelFactory }
    private val binding: FragmentWantToDrinkBinding by dataBinding()
    private val adapter = GroupAdapter<GroupieViewHolder>().apply { spanCount = 2 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wantToDrinkViewModel = wantToDrinkViewModel
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener { wantToDrinkViewModel.refresh() }

        observe()
    }

    private fun observe() {
        observeViewModelDelegate(wantToDrinkViewModel, viewLifecycleOwner)
        wantToDrinkViewModel.groupedWishListWithMode
            .observeNonNull(viewLifecycleOwner) { (groupedWishList, isGrid) ->
                adapter.update(getGroups(groupedWishList, isGrid))
            }
        wantToDrinkViewModel.isGridMode.observeNonNull(viewLifecycleOwner) { isGrid ->
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
                    setHeader(WantToDrinkHeaderItem(region))
                    addAll(sakeList.map { getWantToDrinkGridItem(it, isGrid) })
                }
            }

    private fun getWantToDrinkGridItem(
        sakeDetail: SakeDetail,
        isGrid: Boolean
    ): BindableItem<out ViewDataBinding> {
        return if (isGrid) {
            SakeCardItem(sakeDetail, ::showDrinkDetailFragment)
        } else {
            WantToDrinkLinearItem(sakeDetail, ::showDrinkDetailFragment)
        }
    }

    private fun showDrinkDetailFragment(sakeDetail: SakeDetail) = findNavController().navigate(
        WantToDrinkFragmentDirections.actionWantToDrinkToDrinkDetail(sakeDetail.id)
    )
}
