package jp.kuaddo.tsuidezake.ui

import androidx.lifecycle.ViewModel
import jp.kuaddo.tsuidezake.delegate.LoadingViewModelDelegate
import javax.inject.Inject

class MainViewModel @Inject constructor(
    loadingViewModelDelegate: LoadingViewModelDelegate
) : ViewModel(), LoadingViewModelDelegate by loadingViewModelDelegate
