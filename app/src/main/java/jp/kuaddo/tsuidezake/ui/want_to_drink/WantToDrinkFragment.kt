package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentWantToDrinkBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.DrinkDetail

class WantToDrinkFragment : DaggerFragment() {
    private val binding by dataBinding<FragmentWantToDrinkBinding>(R.layout.fragment_want_to_drink)
    private val adapter = GroupAdapter<GroupieViewHolder>().apply {
        spanCount = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.let { recyclerView ->
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                GridLayoutManager(requireContext(), adapter.spanCount).apply {
                    spanSizeLookup = adapter.spanSizeLookup
                }
        }

        // TODO: remove sample data
        fun getWantToDrinkGridItem(no: Int): WantToDrinkGridItem {
            val drinkDetail = DrinkDetail(
                Drink(no, "秘幻 吟醸酒 $no", "日本酒"),
                no * 10000,
                ""
            )
            return WantToDrinkGridItem(drinkDetail) {
                showDrinkDetailFragment(drinkDetail)
            }
        }

        adapter.addAll(
            listOf(
                Section().apply {
                    setHeader(WantToDrinkHeaderItem("草津"))
                    addAll((1..9).map { getWantToDrinkGridItem(it) })
                },
                Section().apply {
                    setHeader(WantToDrinkHeaderItem("伊香保"))
                    addAll((10..15).map { getWantToDrinkGridItem(it) })
                }
            )
        )
    }

    private fun showDrinkDetailFragment(drinkDetail: DrinkDetail) = findNavController().navigate(
        WantToDrinkFragmentDirections.actionWantToDrinkToDrinkDetail(drinkDetail)
    )
}
