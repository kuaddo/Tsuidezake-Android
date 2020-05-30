package jp.kuaddo.tsuidezake.delegate

import androidx.lifecycle.LiveData
import jp.kuaddo.tsuidezake.util.SnackbarMessage
import jp.kuaddo.tsuidezake.util.live.LiveEvent

interface SnackbarViewModelDelegate {
    val snackbarEvent: LiveData<SnackbarMessage>

    fun setMessage(message: SnackbarMessage)

    fun postMessage(message: SnackbarMessage)
}

class LiveEventSnackbarViewModelDelegate : SnackbarViewModelDelegate {
    override val snackbarEvent: LiveData<SnackbarMessage>
        get() = _snackbarEvent

    private val _snackbarEvent = LiveEvent<SnackbarMessage>()

    override fun setMessage(message: SnackbarMessage) {
        _snackbarEvent.value = message
    }

    override fun postMessage(message: SnackbarMessage) =
        _snackbarEvent.postValue(message)
}
