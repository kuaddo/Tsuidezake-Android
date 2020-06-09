package jp.kuaddo.tsuidezake.delegate

import androidx.lifecycle.LiveData
import jp.kuaddo.tsuidezake.core.live.LiveEvent
import jp.kuaddo.tsuidezake.util.ToastMessage

interface ToastViewModelDelegate {
    val toastEvent: LiveData<ToastMessage>

    fun setMessage(message: ToastMessage)

    fun postMessage(message: ToastMessage)
}

class LiveEventToastViewModelDelegate : ToastViewModelDelegate {
    override val toastEvent: LiveData<ToastMessage>
        get() = _toastEvent

    private val _toastEvent = LiveEvent<ToastMessage>()

    override fun setMessage(message: ToastMessage) {
        _toastEvent.value = message
    }

    override fun postMessage(message: ToastMessage) =
        _toastEvent.postValue(message)
}
