package jp.kuaddo.tsuidezake.ui.wish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetRecommendedSakesUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import jp.kuaddo.tsuidezake.extensions.setValueIfNewAndNotNull
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.LoadingResource
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmptyListInstructionViewModel @Inject constructor(
    private val getRecommendedSakesUseCase: GetRecommendedSakesUseCase,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    private val _recommendedSakes = MutableLiveData<List<Sake>>()
    val recommendedSakes: LiveData<List<Sake>> = _recommendedSakes

    fun loadRecommendedSakes() = viewModelScope.launch {
        var isFirstError = true
        getRecommendedSakesUseCase().collect { res ->
            when (res) {
                is SuccessResource ->
                    _recommendedSakes.setValueIfNew(res.data.take(MAX_GRID_ITEM_COUNT))
                is ErrorResource -> {
                    if (isFirstError) setMessage(SnackbarMessageText(res.message))
                    isFirstError = false
                    _recommendedSakes.setValueIfNewAndNotNull(res.data?.take(MAX_GRID_ITEM_COUNT))
                }
                is LoadingResource ->
                    _recommendedSakes.setValueIfNewAndNotNull(res.data?.take(MAX_GRID_ITEM_COUNT))
            }
        }
    }

    companion object {
        private const val MAX_GRID_ITEM_COUNT = 4
    }
}
