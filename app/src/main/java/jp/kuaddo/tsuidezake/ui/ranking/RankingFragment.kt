package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentRankingBinding
import jp.kuaddo.tsuidezake.databinding.ViewRecommendDrinkBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.DrinkDetail

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
        adapter = RankingAdapter(viewLifecycleOwner) { drink ->
            findNavController().navigate(
                RankingFragmentDirections.actionRankingToDrinkDetail(
                    DrinkDetail(
                        drink,
                        drink.rank * 10000000,
                        "弊社の所在地である獺越の地名の由来は「川上村に古い獺がいて、子供を化かして当村まで追越してきた」ので獺越と称するようになったといわれておりますが（出典；地下上申）、この地名から一字をとって銘柄を「獺祭」と命名しております。獺祭の言葉の意味は、獺が捕らえた魚を岸に並べてまるで祭りをするようにみえるところから、詩や文をつくる時多くの参考資料等を広げちらす事をさします。獺祭から思い起こされるのは、明治の日本文学に革命を起こしたといわれる正岡子規が自らを獺祭書屋主人と号した事です。「酒造りは夢創り、拓こう日本酒新時代」をキャッチフレーズに伝統とか手造りという言葉に安住することなく、変革と革新の中からより優れた酒を創り出そうとする弊社の酒名に「獺祭」と命名した由来はこんな思いからです。"
                    )
                )
            )
        }
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

        showRecommendDrinkDialog()
    }

    private fun showRecommendDrinkDialog() {
        context?.let {
            val drinkBinding = ViewRecommendDrinkBinding.inflate(layoutInflater).apply {
                drinkDetail = DrinkDetail(
                    Drink(1, "獺祭", "日本酒"),
                    10000,
                    ""
                )
                tagsChipGroup.let { chipGroup ->
                    // TODO: remove sample
                    val tagTexts = listOf("辛口", "初心者におすすめ", "コスパ良し")
                    tagTexts.map { Chip(requireContext()).apply { text = it } }
                        .forEach { chip -> chipGroup.addView(chip) }
                }
            }
            drinkBinding.executePendingBindings()
            MaterialDialog(it).show {
                cornerRadius(res = R.dimen.material_dialog_corner_radius)
                lifecycleOwner(viewLifecycleOwner)
                customView(view = drinkBinding.root, noVerticalPadding = true)
            }
        }
    }
}
