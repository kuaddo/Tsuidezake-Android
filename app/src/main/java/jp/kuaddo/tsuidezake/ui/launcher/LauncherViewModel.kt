package jp.kuaddo.tsuidezake.ui.launcher

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.IsAccountInitializedUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    isAccountInitializedUseCase: IsAccountInitializedUseCase,
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    private val initializeTimeIntervalFinished = flow {
        emit(false)
        delay(INITIAL_DELAY)
        emit(true)
    }
    private val timeOutFlow = flow {
        emit(false)
        delay(TIME_OUT_DURATION)
        emit(true)
    }
    val canStart: Flow<Boolean> = combine(
        isAccountInitializedUseCase(),
        initializeTimeIntervalFinished,
        timeOutFlow
    ) { authInitialized, intervalFinished, timeout ->
        (authInitialized && intervalFinished) || timeout
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isVisibleActionBar: LiveData<Boolean>
        get() = _isVisibleActionBar
    private val _isVisibleActionBar = MutableLiveData<Boolean>()

    fun hideActionBar() = _isVisibleActionBar.setValueIfNew(false)

    companion object {
        @VisibleForTesting
        const val INITIAL_DELAY = 2000L

        @VisibleForTesting
        const val TIME_OUT_DURATION = 6000L
    }
}
