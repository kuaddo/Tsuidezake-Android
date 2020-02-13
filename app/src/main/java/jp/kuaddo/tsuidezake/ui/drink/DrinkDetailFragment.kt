package jp.kuaddo.tsuidezake.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentDrinkDetailBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding

class DrinkDetailFragment : DaggerFragment() {
    private val binding by dataBinding<FragmentDrinkDetailBinding>(R.layout.fragment_drink_detail)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tagsChipGroup.let { chipGroup ->
            // TODO: remove sample
            val tagTexts = listOf("辛口", "初心者におすすめ", "コスパ良し")
            tagTexts.map { Chip(requireContext()).apply { text = it } }
                .forEach { chip -> chipGroup.addView(chip) }
        }
    }
}
