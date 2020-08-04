package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.wada811.databinding.dataBinding
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentItemRankingBinding
import jp.kuaddo.tsuidezake.model.Drink

class RankingItemFragment : Fragment(R.layout.fragment_item_ranking) {
    private val binding: FragmentItemRankingBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val rankingAdapter = RankingAdapter(viewLifecycleOwner) { showSakeDetail() }
        binding.recyclerView.let { recyclerView ->
            recyclerView.adapter = rankingAdapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        val sakes = arguments?.getParcelableArray(ARGUMENT_SAKES) ?: emptyArray()
        rankingAdapter.submitList(sakes.map { it as Drink })
    }

    private fun showSakeDetail() = parentFragment?.findNavController()?.navigate(
        RankingFragmentDirections.actionRankingToDrinkDetail()
    )

    companion object {
        private const val ARGUMENT_SAKES = "argumentSakes"

        fun newInstance(sakes: List<Drink>) = RankingItemFragment().apply {
            arguments = Bundle().apply {
                putParcelableArray(ARGUMENT_SAKES, sakes.toTypedArray())
            }
        }
    }
}
