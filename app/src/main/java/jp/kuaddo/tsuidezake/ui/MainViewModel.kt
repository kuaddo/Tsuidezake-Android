package jp.kuaddo.tsuidezake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    loadingViewModelDelegate: LoadingViewModelDelegate
) : ViewModel(), LoadingViewModelDelegate by loadingViewModelDelegate {
    private val _showRankingEvent = MutableSharedFlow<Unit>()
    val showRankingEvent: SharedFlow<Unit> = _showRankingEvent

    fun showRanking() = viewModelScope.launch { _showRankingEvent.emit(Unit) }
}
