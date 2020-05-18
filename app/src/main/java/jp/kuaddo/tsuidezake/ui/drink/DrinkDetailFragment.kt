package jp.kuaddo.tsuidezake.ui.drink

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
            tagTexts.forEach { text -> chipGroup.addTagChip(text) }
        }
    }

    private fun ChipGroup.addTagChip(text: String) {
        val tagChip = View.inflate(requireContext(), R.layout.view_tag_chip, null) as Chip
        tagChip.text = text
        addView(tagChip)
    }
}
