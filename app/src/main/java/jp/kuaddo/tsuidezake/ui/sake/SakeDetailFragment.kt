package jp.kuaddo.tsuidezake.ui.sake

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentSakeDetailBinding
import jp.kuaddo.tsuidezake.extensions.assistedViewModels
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import javax.inject.Inject

class SakeDetailFragment : DaggerFragment(R.layout.fragment_sake_detail) {
    @Inject
    lateinit var viewModelFactory: SakeDetailViewModel.Factory

    private val viewModel: SakeDetailViewModel by assistedViewModels {
        viewModelFactory.create(sakeId = args.sakeId)
    }
    private val args by navArgs<SakeDetailFragmentArgs>()
    private val binding: FragmentSakeDetailBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        observe()
    }

    private fun observe() {
        observeViewModelDelegate(viewModel, viewLifecycleOwner)
        viewModel.sakeDetail.observeNonNull(viewLifecycleOwner) { sakeDetail ->
            binding.tagsChipGroup.let { chipGroup ->
                chipGroup.removeAllViews()
                sakeDetail.tags.forEach { tag -> chipGroup.addTagChip(tag.name) }
            }
        }
        viewModel.showEvaluationDialogEvent.observeNonNull(viewLifecycleOwner) {
            // TODO: Show evaluation dialog
            viewModel.addSakeToTastedList(3) // TODO: Replace dummy value.
        }
    }

    private fun ChipGroup.addTagChip(text: String) {
        val tagChip = View.inflate(requireContext(), R.layout.view_tag_chip, null) as Chip
        tagChip.text = text
        addView(tagChip)
    }
}
