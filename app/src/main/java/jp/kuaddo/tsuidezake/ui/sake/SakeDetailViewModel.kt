package jp.kuaddo.tsuidezake.ui.sake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.AddSakeToTastedListUseCase
import jp.kuaddo.tsuidezake.domain.AddSakeToWishListUseCase
import jp.kuaddo.tsuidezake.domain.GetSakeDetailUseCase
import jp.kuaddo.tsuidezake.domain.RemoveSakeFromTastedListUseCase
import jp.kuaddo.tsuidezake.domain.RemoveSakeFromWishListUseCase
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageRes
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.launch

class SakeDetailViewModel @AssistedInject constructor(
    @Assisted private val sakeId: Int,
    private val getSakeDetailUseCase: GetSakeDetailUseCase,
    private val addSakeToWishListUseCase: AddSakeToWishListUseCase,
    private val removeSakeFromWishListUseCase: RemoveSakeFromWishListUseCase,
    private val addSakeToTastedListUseCase: AddSakeToTastedListUseCase,
    private val removeSakeFromTastedListUseCase: RemoveSakeFromTastedListUseCase,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val sakeDetail: LiveData<SakeDetail> = liveData {
        when (val res = getSakeDetailUseCase(sakeId)) {
            is SuccessResource -> emit(res.data)
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }

    // TODO: この部分はCustomViewに責務を分けたい
    val isExpanded: LiveData<Boolean>
        get() = _isExpanded
    val isAddedToWish: LiveData<Boolean>
        get() = _isAddedToWish
    val isAddedToTasted: LiveData<Boolean>
        get() = _isAddedToTasted

    private val _isExpanded = MutableLiveData(false)
    private val _isAddedToWish = MutableLiveData(false)
    private val _isAddedToTasted = MutableLiveData(false)

    fun switchExpandState() {
        _isExpanded.value = _isExpanded.value?.not() ?: true
    }

    fun toggleWishState() = viewModelScope.launch {
        // TODO: show loading UI
        if (_isAddedToWish.value == true) removeSakeFromWishList()
        else addSakeToWishList()
    }

    fun toggleTastedState() = viewModelScope.launch {
        // TODO: show loading UI
        if (_isAddedToTasted.value == true) removeSakeFromTastedList()
        else addSakeToTastedList()
    }

    private suspend fun addSakeToWishList() {
        when (addSakeToWishListUseCase(sakeId)) {
            is SuccessResource -> _isAddedToWish.value = true
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_add_wish_list_failed))
        }
    }

    private suspend fun removeSakeFromWishList() {
        when (removeSakeFromWishListUseCase(sakeId)) {
            is SuccessResource -> _isAddedToWish.value = false
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_remove_wish_list_failed))
        }
    }

    private suspend fun addSakeToTastedList() {
        when (addSakeToTastedListUseCase(sakeId)) {
            is SuccessResource -> _isAddedToTasted.value = true
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_add_tasted_list_failed))
        }
    }

    private suspend fun removeSakeFromTastedList() {
        when (removeSakeFromTastedListUseCase(sakeId)) {
            is SuccessResource -> _isAddedToTasted.value = false
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_remove_tasted_list_failed))
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(sakeId: Int): SakeDetailViewModel
    }
}
