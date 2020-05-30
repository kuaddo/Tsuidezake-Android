package jp.kuaddo.tsuidezake.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import kotlinx.coroutines.delay
import javax.inject.Inject

class LauncherViewModel @Inject constructor(
    loadingViewModelDelegate: LoadingViewModelDelegate,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    LoadingViewModelDelegate by loadingViewModelDelegate,
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val isInitialized: LiveData<Boolean> = liveData {
        delay(2000L)
        emit(true)
    }

    val isVisibleActionBar: LiveData<Boolean>
        get() = _isVisibleActionBar

    private val _isVisibleActionBar = MutableLiveData<Boolean>()

    fun showActionBar() = _isVisibleActionBar.setValueIfNew(true)

    fun hideActionBar() = _isVisibleActionBar.setValueIfNew(false)
}
