package jp.kuaddo.tsuidezake.ui.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetRankingsUseCase
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

class RankingViewModel @Inject constructor(
    getRecommendedSakesUseCase: GetRecommendedSakesUseCase,
    private val getRankingsUseCase: GetRankingsUseCase,
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    private val _recommendedSakes = MutableLiveData<List<Sake>>()
    val recommendedSakes: LiveData<List<Sake>> = _recommendedSakes

    val rankings = liveData {
        when (val res = getRankingsUseCase()) {
            is SuccessResource -> emit(res.data)
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }

    init {
        collectRecommendedSakes(getRecommendedSakesUseCase)
    }

    private fun collectRecommendedSakes(
        useCase: GetRecommendedSakesUseCase
    ) = viewModelScope.launch {
        var isFirstError = true
        useCase().collect { res ->
            when (res) {
                is SuccessResource -> _recommendedSakes.setValueIfNew(res.data)
                is ErrorResource -> {
                    if (isFirstError) setMessage(SnackbarMessageText(res.message))
                    isFirstError = false
                    _recommendedSakes.setValueIfNewAndNotNull(res.data)
                }
                is LoadingResource -> _recommendedSakes.setValueIfNewAndNotNull(res.data)
            }
        }
    }
}
