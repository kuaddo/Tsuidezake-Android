package jp.kuaddo.tsuidezake.ui.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText

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

    private val _isExpanded = MutableLiveData<Boolean>()

    fun switchExpandState() {
        _isExpanded.value = _isExpanded.value?.not() ?: true
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(sakeId: Int): DrinkDetailViewModel
    }
}
