package jp.kuaddo.tsuidezake.delegate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

interface LoadingViewModelDelegate {

    /**
     * 全てのソースがLoading中でない場合に、[isLoading]がfalseになる
     */
    val isLoading: LiveData<Boolean>

    /**
     * ViewModelのLoadingを開始する
     */
    fun startViewModelLoading()

    /**
     * ViewModelのLoadingを終了する。[addLoadingSource]で追加した他のソースは終了しない
     */
    fun stopViewModelLoading()

    /**
     * Loadingのソースを追加する
     */
    fun addLoadingSource(source: LiveData<Boolean>)
}

class MediatorLoadingViewModelDelegate : LoadingViewModelDelegate {
    override val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isLoading = MediatorLiveData<Boolean>()
    private val isViewModelLoading = MutableLiveData<Boolean>()
    private val sources = mutableListOf<LiveData<Boolean>>()

    init {
        addLoadingSource(isViewModelLoading)
    }

    override fun startViewModelLoading() {
        isViewModelLoading.value = true
    }

    override fun stopViewModelLoading() {
        isViewModelLoading.value = false
    }

    override fun addLoadingSource(source: LiveData<Boolean>) {
        sources += source
        _isLoading.addSource(source) { onChanged() }
    }

    private fun onChanged() {
        _isLoading.value = sources.any { it.value ?: false }
    }
}
