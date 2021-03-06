package jp.kuaddo.tsuidezake.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import jp.kuaddo.tsuidezake.delegate.SnackbarViewModelDelegate
import jp.kuaddo.tsuidezake.delegate.ToastViewModelDelegate
import jp.kuaddo.tsuidezake.di.AssistedViewModelFactory
import jp.kuaddo.tsuidezake.util.SnackbarMessage
import jp.kuaddo.tsuidezake.util.SnackbarMessageRes
import jp.kuaddo.tsuidezake.util.SnackbarMessageText
import jp.kuaddo.tsuidezake.util.ToastMessage
import jp.kuaddo.tsuidezake.util.ToastMessageRes
import jp.kuaddo.tsuidezake.util.ToastMessageText
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Fragment.observeViewModelDelegate(
    viewModel: ViewModel,
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner
) {
    if (viewModel is SnackbarViewModelDelegate) {
        observeSnackbar(viewModel.snackbarEvent, lifecycleOwner)
    }
    if (viewModel is ToastViewModelDelegate) {
        observeToast(viewModel.toastEvent, lifecycleOwner)
    }
}

/**
 * A lazy property that gets cleaned up when the fragment's view is destroyed.
 *
 * Accessing this variable while the fragment's view is destroyed will throw NPE.
 * ref: https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/util/AutoClearedValue.kt
 */
class AutoClearedValue<T : Any>(val fragment: Fragment) : ReadWriteProperty<Fragment, T> {
    private var _value: T? = null

    init {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeNonNull(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(
                        object : DefaultLifecycleObserver {
                            override fun onDestroy(owner: LifecycleOwner) {
                                _value = null
                            }
                        }
                    )
                }
            }
        }
        fragment.lifecycle.addObserver(observer)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
            "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }
}

/**
 * Creates an [AutoClearedValue] associated with this fragment.
 */
fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)

inline fun <reified VM : ViewModel> Fragment.assistedViewModels(
    crossinline create: () -> VM
): Lazy<VM> = viewModels { AssistedViewModelFactory { create() } }

inline fun <reified VM : ViewModel> Fragment.assistedActivityViewModels(
    crossinline create: () -> VM
): Lazy<VM> = activityViewModels { AssistedViewModelFactory { create() } }

private fun Fragment.observeSnackbar(
    snackbarEvent: LiveData<SnackbarMessage>,
    lifecycleOwner: LifecycleOwner
) {
    snackbarEvent.observeNonNull(lifecycleOwner) { message ->
        when (message) {
            is SnackbarMessageRes -> view?.let {
                val text = getString(message.resId, *message.params)
                Snackbar.make(it, text, message.duration).show()
            }
            is SnackbarMessageText -> view?.let {
                Snackbar.make(it, message.text, message.duration).show()
            }
        }
    }
}

private fun Fragment.observeToast(
    toastEvent: LiveData<ToastMessage>,
    lifecycleOwner: LifecycleOwner
) {
    toastEvent.observeNonNull(lifecycleOwner) { message ->
        when (message) {
            is ToastMessageRes -> context?.let {
                val text = getString(message.resId, *message.params)
                Toast.makeText(it, text, message.duration).show()
            }
            is ToastMessageText -> context?.let {
                Toast.makeText(it, message.text, message.duration).show()
            }
        }
    }
}
