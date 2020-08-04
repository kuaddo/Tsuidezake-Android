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
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.DrinkDetail
import javax.inject.Inject

class WantToDrinkFragment : DaggerFragment(R.layout.fragment_want_to_drink) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wantToDrinkViewModel: WantToDrinkViewModel by viewModels { viewModelFactory }
    private val binding: FragmentWantToDrinkBinding by dataBinding()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.wantToDrinkViewModel = wantToDrinkViewModel
        binding.recyclerView.adapter = adapter

        observe()
    }

    private fun observe() {
        wantToDrinkViewModel.isGridMode.observeNonNull(viewLifecycleOwner) { isGrid ->
            adapter.update(getGroups(isGrid))
            adapter.spanCount = 2
            binding.recyclerView.layoutManager = getLayoutManager(isGrid)
        }
    }

    // TODO: remove sample data
    private fun getWantToDrinkGridItem(
        no: Int,
        isGrid: Boolean
    ): BindableItem<out ViewDataBinding> {
        val drinkDetail = DrinkDetail(
            Drink(no, "秘幻 吟醸酒 $no", "日本酒"),
            no * 10000,
            ""
        )
        return if (isGrid) {
            SakeCardItem(drinkDetail) {
                showDrinkDetailFragment(drinkDetail)
            }
        } else {
            WantToDrinkLinearItem(drinkDetail) {
                showDrinkDetailFragment(drinkDetail)
            }
        }
    }

    private fun getLayoutManager(isGrid: Boolean) = if (isGrid) {
        GridLayoutManager(requireContext(), adapter.spanCount).apply {
            spanSizeLookup = adapter.spanSizeLookup
        }
    } else {
        LinearLayoutManager(requireContext())
    }

    private fun getGroups(isGrid: Boolean) = listOf(
        Section().apply {
            setHeader(WantToDrinkHeaderItem("草津"))
            addAll((1..9).map { getWantToDrinkGridItem(it, isGrid) })
        },
        Section().apply {
            setHeader(WantToDrinkHeaderItem("伊香保"))
            addAll((10..15).map { getWantToDrinkGridItem(it, isGrid) })
        }
    )

    private fun showDrinkDetailFragment(drinkDetail: DrinkDetail) = findNavController().navigate(
        WantToDrinkFragmentDirections.actionWantToDrinkToDrinkDetail(drinkDetail)
    )
}
