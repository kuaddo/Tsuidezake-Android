package jp.kuaddo.tsuidezake.ui.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class DrinkDetailViewModel @Inject constructor() : ViewModel() {
    // TODO: この部分はCustomViewに責務を分けたい
    val isExpanded: LiveData<Boolean>
        get() = _isExpanded

    private val _isExpanded = MutableLiveData<Boolean>()

    fun switchExpandState() {
        _isExpanded.value = _isExpanded.value?.not() ?: true
    }
}
