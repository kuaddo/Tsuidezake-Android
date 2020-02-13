package jp.kuaddo.tsuidezake.ui.want_to_drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentWantToDrinkBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.DrinkDetail
import jp.kuaddo.tsuidezake.ui.common.TwoColumnGridItemOffsetDecoration

class WantToDrinkFragment : DaggerFragment() {
    private val binding by dataBinding<FragmentWantToDrinkBinding>(R.layout.fragment_want_to_drink)
    private lateinit var adapter: WantToDrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = WantToDrinkAdapter(viewLifecycleOwner) { drinkDetail ->
            findNavController().navigate(
                WantToDrinkFragmentDirections.actionWantToDrinkToDrinkDetail(drinkDetail)
            )
        }
        binding.recyclerView.let { recyclerView ->
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                TwoColumnGridItemOffsetDecoration(requireContext(), offsetDp = 8)
            )
        }

        // TODO: remove sample data
        adapter.submitList(
            (1..15).map {
                DrinkDetail(
                    Drink(it, "獺祭$it", "日本酒"),
                    it * 10000,
                    ""
                )
            }
        )
    }
}
