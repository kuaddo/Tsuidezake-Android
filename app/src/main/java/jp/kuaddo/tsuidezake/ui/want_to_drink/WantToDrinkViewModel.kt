package jp.kuaddo.tsuidezake.ui.want_to_drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import jp.kuaddo.tsuidezake.data.repository.Repository
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.extensions.combineLatest
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import javax.inject.Inject

typealias GroupedWishList = Map<String, List<SakeDetail>>

class WantToDrinkViewModel @Inject constructor(
    private val repository: Repository,
    snackbarViewModelDelegate: SnackbarViewModelDelegate
) : ViewModel(),
    SnackbarViewModelDelegate by snackbarViewModelDelegate {

    val groupedWishListWithMode: LiveData<Pair<GroupedWishList, Boolean>>
        get() = groupedWishList.combineLatest(_isGridMode) { list, mode -> list to mode }
    val isGridMode: LiveData<Boolean>
        get() = _isGridMode

    private val groupedWishList: LiveData<GroupedWishList> = liveData {
        when (val res = repository.getWishList()) {
            is SuccessResource -> emit(res.data.groupBy { it.region })
            is ErrorResource -> setMessage(SnackbarMessageText(res.message))
        }
    }

    private val _isGridMode = MutableLiveData(true)

    fun switchRecyclerViewMode() {
        _isGridMode.value = _isGridMode.value?.not()
    }
}
