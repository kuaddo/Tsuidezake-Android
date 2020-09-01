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
import jp.kuaddo.tsuidezake.model.Ranking

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

        val contents = arguments?.getParcelableArray(ARGUMENT_CONTENTS) ?: emptyArray()
        // TODO: Fix to pass content directly.
        val drinks = contents.map {
            val content = it as Ranking.Content
            Drink(content.rank, content.name, "")
        }
        rankingAdapter.submitList(drinks)
    }

    private fun showSakeDetail() = parentFragment?.findNavController()?.navigate(
        RankingFragmentDirections.actionRankingToDrinkDetail()
    )

    companion object {
        private const val ARGUMENT_CONTENTS = "argumentContents"

        fun newInstance(contents: List<Ranking.Content>) = RankingItemFragment().apply {
            arguments = Bundle().apply {
                putParcelableArray(ARGUMENT_CONTENTS, contents.toTypedArray())
            }
        }
    }
}
