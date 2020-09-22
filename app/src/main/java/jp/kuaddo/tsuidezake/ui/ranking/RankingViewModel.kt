package jp.kuaddo.tsuidezake.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetRankingsUseCase
import jp.kuaddo.tsuidezake.domain.GetRecommendedSakesUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import javax.inject.Inject

class RankingViewModel @Inject constructor(
    private val getRecommendedSakesUseCase: GetRecommendedSakesUseCase,
    private val getRankingsUseCase: GetRankingsUseCase,
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val recommendedSakes = liveData {
        when (val res = getRecommendedSakesUseCase()) {
            is SuccessResource -> emit(res.data)
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }

    val rankings = liveData {
        when (val res = getRankingsUseCase()) {
            is SuccessResource -> emit(res.data)
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }
}
