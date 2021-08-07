package jp.kuaddo.tsuidezake.ui.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetRankingsUseCase
import jp.kuaddo.tsuidezake.domain.GetRecommendedSakesUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import jp.kuaddo.tsuidezake.extensions.setValueIfNewAndNotNull
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.LoadingResource
import jp.kuaddo.tsuidezake.model.Ranking
import jp.kuaddo.tsuidezake.model.Sake
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    getRecommendedSakesUseCase: GetRecommendedSakesUseCase,
    getRankingsUseCase: GetRankingsUseCase,
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    private val _recommendedSakes = MutableLiveData<List<Sake>>()
    val recommendedSakes: LiveData<List<Sake>> = _recommendedSakes

    private val _rankings = MutableLiveData<List<Ranking>>()
    val rankings: LiveData<List<Ranking>> = _rankings

    init {
        collectRecommendedSakes(getRecommendedSakesUseCase)
        collectRankings(getRankingsUseCase)
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

    private fun collectRankings(useCase: GetRankingsUseCase) = viewModelScope.launch {
        var isFirstError = true
        useCase().collect { res ->
            when (res) {
                is SuccessResource ->
                    _rankings.setValueIfNewAndNotNull(res.data.takeIf { it.isNotEmpty() })
                is ErrorResource -> {
                    if (isFirstError) setMessage(SnackbarMessageText(res.message))
                    isFirstError = false
                    _rankings.setValueIfNewAndNotNull(res.data.takeIf { !it.isNullOrEmpty() })
                }
                is LoadingResource ->
                    _rankings.setValueIfNewAndNotNull(res.data.takeIf { !it.isNullOrEmpty() })
            }
        }
    }
}
