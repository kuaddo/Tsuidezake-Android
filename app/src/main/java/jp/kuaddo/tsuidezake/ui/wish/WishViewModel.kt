package jp.kuaddo.tsuidezake.ui.wish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetWishListUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.combineLatest
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias GroupedWishList = Map<String, List<SakeDetail>>

class WishViewModel @Inject constructor(
    private val getWishListUseCase: GetWishListUseCase,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing
    val isGridMode: LiveData<Boolean>
        get() = _isGridMode
    val groupedWishListWithMode: LiveData<Pair<GroupedWishList, Boolean>>
        get() = _groupedWishList.combineLatest(_isGridMode) { list, mode -> list to mode }

    private val _isRefreshing = MutableLiveData(false)
    private val _isGridMode = MutableLiveData(true)
    private val _groupedWishList = MutableLiveData<GroupedWishList>()

    init {
        updateWishList()
    }

    fun switchRecyclerViewMode() {
        _isGridMode.value = _isGridMode.value?.not()
    }

    fun refresh() = viewModelScope.launch {
        _isRefreshing.value = true
        updateWishList().join()
        _isRefreshing.value = false
    }

    private fun updateWishList() = viewModelScope.launch {
        when (val res = getWishListUseCase()) {
            is SuccessResource -> _groupedWishList.value = res.data.groupBy { it.region }
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }
}
