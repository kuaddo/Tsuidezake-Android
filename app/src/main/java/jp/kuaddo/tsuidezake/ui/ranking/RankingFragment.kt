package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentRankingBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.model.Drink

class RankingFragment : DaggerFragment() {
    private val binding by dataBinding<FragmentRankingBinding>(R.layout.fragment_ranking)
    private lateinit var adapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RankingAdapter(viewLifecycleOwner)
        binding.recyclerView.let { recyclerView ->
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        // TODO: remove sample data
        adapter.submitList(
            (1..20).map { Drink(it, "獺祭$it", "日本酒") }
        )
    }
}
