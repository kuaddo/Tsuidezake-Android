package jp.kuaddo.tsuidezake.ui.sake

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.AddSakeToTastedListUseCase
import jp.kuaddo.tsuidezake.domain.AddSakeToWishListUseCase
import jp.kuaddo.tsuidezake.domain.GetUserSakeUseCase
import jp.kuaddo.tsuidezake.domain.RemoveSakeFromTastedListUseCase
import jp.kuaddo.tsuidezake.domain.RemoveSakeFromWishListUseCase
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import jp.kuaddo.tsuidezake.extensions.setValueIfNewAndNotNull
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.LoadingResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.model.UserSake
import jp.kuaddo.tsuidezake.util.SnackbarMessageRes
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Assisted injectionではなくHilt + SavedStateHandleを検討する。
//  Installされるスコープに問題があり、リークする可能性があるらしい
//  https://github.com/google/dagger/issues/2287
class SakeDetailViewModel @AssistedInject constructor(
    @Assisted private val sakeId: Int,
    private val getUserSakeUseCase: GetUserSakeUseCase,
    private val addSakeToWishListUseCase: AddSakeToWishListUseCase,
    private val removeSakeFromWishListUseCase: RemoveSakeFromWishListUseCase,
    private val addSakeToTastedListUseCase: AddSakeToTastedListUseCase,
    private val removeSakeFromTastedListUseCase: RemoveSakeFromTastedListUseCase,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    @AssistedFactory
    interface Factory {
        fun create(sakeId: Int): SakeDetailViewModel
    }

    private val userSake = MutableLiveData<UserSake>()
    val sakeDetail: LiveData<SakeDetail> = userSake.map(UserSake::sakeDetail)
    val isAddedToWish: LiveData<Boolean> = userSake.map(UserSake::isAddedToWish)
    val isAddedToTasted: LiveData<Boolean> = userSake.map(UserSake::isAddedToTasted)

    private val _showEvaluationDialogEvent = LiveEvent<Unit>()
    val showEvaluationDialogEvent: LiveData<Unit> = _showEvaluationDialogEvent

    private val _showTastedScreenEvent = LiveEvent<Unit>()
    val showTastedScreenEvent: LiveData<Unit> = _showTastedScreenEvent

    // TODO: この部分はCustomViewに責務を分けたい
    val isExpanded: LiveData<Boolean>
        get() = _isExpanded

    private val _isExpanded = MutableLiveData(false)

    init {
        viewModelScope.launch {
            var isFirstError = true
            getUserSakeUseCase(sakeId).collect { res ->
                when (res) {
                    is SuccessResource -> userSake.setValueIfNew(res.data)
                    is ErrorResource -> {
                        if (isFirstError) setMessage(SnackbarMessageText(res.message))
                        isFirstError = false
                        userSake.setValueIfNewAndNotNull(res.data)
                    }
                    is LoadingResource -> {
                        // TODO: show loading UI
                        userSake.setValueIfNewAndNotNull(res.data)
                    }
                }
            }
        }
    }

    fun switchExpandState() {
        _isExpanded.value = _isExpanded.value?.not() ?: true
    }

    fun toggleWishState() = viewModelScope.launch {
        // TODO: show loading UI
        if (isAddedToWish.value == true) removeSakeFromWishList()
        else addSakeToWishList()
    }

    fun toggleTastedState() = viewModelScope.launch {
        // TODO: show loading UI
        if (isAddedToTasted.value == true) removeSakeFromTastedList()
        else _showEvaluationDialogEvent.value = Unit
    }

    private suspend fun addSakeToWishList() {
        when (addSakeToWishListUseCase(sakeId)) {
            is SuccessResource -> userSake.value = userSake.value?.copy(isAddedToWish = true)
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_add_wish_list_failed))
        }
    }

    private suspend fun removeSakeFromWishList() {
        when (removeSakeFromWishListUseCase(sakeId)) {
            is SuccessResource -> userSake.value = userSake.value?.copy(isAddedToWish = false)
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_remove_wish_list_failed))
        }
    }

    fun addSakeToTastedList(
        @IntRange(from = 1, to = 5) evaluation: Int,
        shouldShowTastedScreen: Boolean
    ) = viewModelScope.launch {
        val parameter = AddSakeToTastedListUseCase.Parameter(sakeId, evaluation)
        when (addSakeToTastedListUseCase(parameter)) {
            is SuccessResource -> {
                userSake.value = userSake.value?.copy(isAddedToTasted = true)
                if (shouldShowTastedScreen) _showTastedScreenEvent.value = Unit
            }
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_add_tasted_list_failed))
        }
    }

    private suspend fun removeSakeFromTastedList() {
        when (removeSakeFromTastedListUseCase(sakeId)) {
            is SuccessResource -> userSake.value = userSake.value?.copy(isAddedToTasted = false)
            is ErrorResource ->
                setMessage(SnackbarMessageRes(R.string.sake_detail_remove_tasted_list_failed))
        }
    }
}
