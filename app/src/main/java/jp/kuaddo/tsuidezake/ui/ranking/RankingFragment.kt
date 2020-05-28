package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentRankingBinding
import jp.kuaddo.tsuidezake.extensions.autoCleared
import jp.kuaddo.tsuidezake.model.Drink
import jp.kuaddo.tsuidezake.model.DrinkDetail

class RankingFragment : DaggerFragment(R.layout.fragment_ranking) {

    private val binding: FragmentRankingBinding by dataBinding()
    private var adapter: RankingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RankingAdapter(viewLifecycleOwner) { drink ->
            findNavController().navigate(
                RankingFragmentDirections.actionRankingToDrinkDetail(
                    DrinkDetail(
                        drink,
                        drink.rank * 10000000,
                        "弊社の所在地である獺越の地名の由来は「川上村に古い獺がいて、" +
                            "子供を化かして当村まで追越してきた」ので獺越と称するようになったと" +
                            "いわれておりますが（出典；地下上申）、この地名から一字をとって銘柄を" +
                            "「獺祭」と命名しております。獺祭の言葉の意味は、獺が捕らえた魚を" +
                            "岸に並べてまるで祭りをするようにみえるところから、詩や文をつくる" +
                            "時多くの参考資料等を広げちらす事をさします。" +
                            "獺祭から思い起こされるのは、明治の日本文学に革命を起こしたといわれる" +
                            "正岡子規が自らを獺祭書屋主人と号した事です。「酒造りは夢創り、" +
                            "拓こう日本酒新時代」をキャッチフレーズに伝統とか手造りという言葉に" +
                            "安住することなく、変革と革新の中からより優れた酒を創り出そうとする" +
                            "弊社の酒名に「獺祭」と命名した由来はこんな思いからです。"
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

    private fun showRecommendDrinkDialog() =
        findNavController().navigate(RankingFragmentDirections.actionRankingToSwipeSortingDialog())
}
