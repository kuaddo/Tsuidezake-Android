package jp.kuaddo.tsuidezake.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.DialogSwipeSortingBinding
import jp.kuaddo.tsuidezake.extensions.autoCleared
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.Tag

class SwipeSortingDialogFragment : DialogFragment() {

    private var binding: DialogSwipeSortingBinding by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSwipeSortingBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swipeSortingView.apply {
            val tags = listOf("辛口", "初心者におすすめ", "コスパ良し")
                .mapIndexed { index, name -> Tag(id = index, name = name) }
            val sakes = (1..5).map { index ->
                SakeDetail(
                    id = index,
                    name = "獺祭$index",
                    description = null,
                    region = "草津",
                    brewer = null,
                    imageUri = null,
                    tags = tags,
                    suitableTemperatures = emptySet(),
                    goodFoodCategories = emptySet()
                )
            }
            submitSakes(sakes)
            onLastSakeRemoved = { dialog?.dismiss() }
        }
    }
}
