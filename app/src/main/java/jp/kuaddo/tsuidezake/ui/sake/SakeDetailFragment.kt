package jp.kuaddo.tsuidezake.ui.sake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.FragmentSakeDetailBinding
import jp.kuaddo.tsuidezake.databinding.ViewSakeEvaluationBinding
import jp.kuaddo.tsuidezake.extensions.assistedViewModels
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.observeViewModelDelegate
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SakeDetailFragment : Fragment(R.layout.fragment_sake_detail) {
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
            showEvaluationDialog()
        }
        viewModel.showTastedScreenEvent.observeNonNull(viewLifecycleOwner) {
            Timber.d("Show tasted fragment")
            // TODO: Show tasted fragment
        }
    }

    private fun ChipGroup.addTagChip(text: String) {
        val tagChip = View.inflate(requireContext(), R.layout.view_tag_chip, null) as Chip
        tagChip.text = text
        addView(tagChip)
    }

    private fun showEvaluationDialog() {
        val nonNullContext = context ?: return
        val evaluationBinding = ViewSakeEvaluationBinding
            .inflate(LayoutInflater.from(nonNullContext))
        evaluationBinding.lifecycleOwner = viewLifecycleOwner
        evaluationBinding.evaluation = DEFAULT_EVALUATION
        listOf(
            evaluationBinding.star1,
            evaluationBinding.star2,
            evaluationBinding.star3,
            evaluationBinding.star4,
            evaluationBinding.star5
        ).forEachIndexed { index, starImage ->
            starImage.setOnClickListener { evaluationBinding.evaluation = index + 1 }
        }

        MaterialDialog(nonNullContext).show {
            title(R.string.evaluation_dialog_title)
            customView(view = evaluationBinding.root)
            positiveButton(R.string.done) {
                viewModel.addSakeToTastedList(
                    evaluationBinding.evaluation,
                    evaluationBinding.showTastedScreenCheckBox.isChecked
                )
            }
            negativeButton()
        }
    }

    companion object {
        private const val DEFAULT_EVALUATION = 3
    }
}
