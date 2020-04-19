package jp.kuaddo.tsuidezake.ui.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentDrinkDetailBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import javax.inject.Inject

class DrinkDetailFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DrinkDetailViewModel by viewModels { viewModelFactory }
    private val args by navArgs<DrinkDetailFragmentArgs>()
    private val binding by dataBinding<FragmentDrinkDetailBinding>(R.layout.fragment_drink_detail)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.let {
            it.drinkDetail = args.drinkDetail
            it.viewModel = viewModel
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tagsChipGroup.let { chipGroup ->
            // TODO: remove sample
            val tagTexts = listOf("辛口", "初心者におすすめ", "コスパ良し")
            tagTexts.map { createChip(it) }.forEach { chip -> chipGroup.addView(chip) }
        }
    }

    private fun createChip(text: String): Chip {
        val tagChip = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_tag_chip, null) as Chip
        tagChip.text = text
        return tagChip
    }
}
