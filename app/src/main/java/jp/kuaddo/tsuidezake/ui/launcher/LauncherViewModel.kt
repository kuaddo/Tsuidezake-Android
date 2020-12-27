package jp.kuaddo.tsuidezake.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.IsAccountInitializedUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.combineLatest
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import kotlinx.coroutines.delay
import javax.inject.Inject

class LauncherViewModel @Inject constructor(
    private val isAccountInitializedUseCase: IsAccountInitializedUseCase,
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val canStart: LiveData<Boolean>
        get() = isAccountInitializedUseCase().asLiveData()
            .combineLatest(initializeTimeIntervalFinished) { authInitialized, intervalFinished ->
                authInitialized && intervalFinished
            }
            .combineLatest(timeOutLiveData) { isInitialized, timeOut ->
                isInitialized || timeOut
            }
    private val initializeTimeIntervalFinished = liveData {
        delay(INITIAL_DELAY)
        emit(true)
    }
    private val timeOutLiveData = liveData {
        emit(false)
        delay(TIME_OUT_DURATION)
        emit(true)
    }

    val isVisibleActionBar: LiveData<Boolean>
        get() = _isVisibleActionBar
    private val _isVisibleActionBar = MutableLiveData<Boolean>()

    fun showActionBar() = _isVisibleActionBar.setValueIfNew(true)

    fun hideActionBar() = _isVisibleActionBar.setValueIfNew(false)

    companion object {
        const val INITIAL_DELAY = 2000L
        const val TIME_OUT_DURATION = 6000L
    }
}
