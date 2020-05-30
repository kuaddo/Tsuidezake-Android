package jp.kuaddo.tsuidezake.ui.want_to_drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class WantToDrinkViewModel @Inject constructor() : ViewModel() {
    val isGridMode: LiveData<Boolean>
        get() = _isGridMode

    private val _isGridMode = MutableLiveData(true)

    fun switchRecyclerViewMode() {
        _isGridMode.value = _isGridMode.value?.not()
    }
}
