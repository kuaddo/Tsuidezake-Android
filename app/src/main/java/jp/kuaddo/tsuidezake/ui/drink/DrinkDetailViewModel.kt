package jp.kuaddo.tsuidezake.ui.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageRes
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.launch

class DrinkDetailViewModel @AssistedInject constructor(
    @Assisted private val sakeId: Int,
    private val repository: Repository,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val sakeDetail: LiveData<SakeDetail> = liveData {
        when (val res = repository.getSakeDetail(sakeId)) {
            is SuccessResource -> emit(res.data)
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }

    // TODO: この部分はCustomViewに責務を分けたい
    val isExpanded: LiveData<Boolean>
        get() = _isExpanded
    val isAddedToWith: LiveData<Boolean>
        get() = _isAddedToWish

    private val _isExpanded = MutableLiveData(false)
    private val _isAddedToWish = MutableLiveData(false)

    fun switchExpandState() {
        _isExpanded.value = _isExpanded.value?.not() ?: true
    }

    fun toggleWithState() = viewModelScope.launch {
        // TODO: show loading UI
        if (_isAddedToWish.value == true) removeSakeFromWishList()
        else addSakeToWishList()
    }

    private suspend fun addSakeToWishList() {
        when (repository.addSakeToWishList(sakeId)) {
            is SuccessResource -> _isAddedToWish.value = true
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_add_wish_list_failed))
        }
    }

    private suspend fun removeSakeFromWishList() {
        when (repository.removeSakeFromWishList(sakeId)) {
            is SuccessResource -> _isAddedToWish.value = false
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_remove_wish_list_failed))
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(sakeId: Int): DrinkDetailViewModel
    }
}
