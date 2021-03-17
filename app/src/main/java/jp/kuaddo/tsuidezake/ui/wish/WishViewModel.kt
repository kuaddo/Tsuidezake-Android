package jp.kuaddo.tsuidezake.ui.wish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.domain.GetWishListUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.extensions.combineLatest
import jp.kuaddo.tsuidezake.extensions.setValueIfNew
import jp.kuaddo.tsuidezake.extensions.setValueIfNewAndNotNull
import jp.kuaddo.tsuidezake.model.ErrorResource
import jp.kuaddo.tsuidezake.model.LoadingResource
import jp.kuaddo.tsuidezake.model.SakeDetail
import jp.kuaddo.tsuidezake.model.SuccessResource
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
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
    val hasWish: LiveData<Boolean>
        get() = _groupedWishList.map { !it.isNullOrEmpty() }
    val groupedWishListWithMode: LiveData<Pair<GroupedWishList, Boolean>>
        get() = _groupedWishList.combineLatest(_isGridMode) { list, mode -> list to mode }

    private val _isRefreshing = MutableLiveData(false)
    private val _isGridMode = MutableLiveData(true)
    private val _groupedWishList = MutableLiveData<GroupedWishList>()

    private var getWishListJob: Job? = null

    init {
        collect(getWishListUseCase)
    }

    fun switchRecyclerViewMode() {
        _isGridMode.value = _isGridMode.value?.not()
    }

    fun refresh() = collect(getWishListUseCase)

    private fun collect(getWishListUseCase: GetWishListUseCase) {
        getWishListJob?.cancel()
        getWishListJob = viewModelScope.launch {
            var isFirstError = true
            getWishListUseCase().collect { res ->
                when (res) {
                    is SuccessResource -> {
                        _isRefreshing.value = false
                        _groupedWishList.setValueIfNew(res.data.toGroupedWishList())
                    }
                    is ErrorResource -> {
                        if (isFirstError) setMessage(SnackbarMessageText(res.message))
                        isFirstError = false
                        _isRefreshing.value = false
                        _groupedWishList.setValueIfNewAndNotNull(res.data?.toGroupedWishList())
                    }
                    is LoadingResource -> {
                        _isRefreshing.value = true
                        _groupedWishList.setValueIfNewAndNotNull(res.data?.toGroupedWishList())
                    }
                }
            }
        }
    }

    companion object {
        fun List<SakeDetail>.toGroupedWishList() = groupBy { it.region }
    }
}
