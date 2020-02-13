package jp.kuaddo.tsuidezake.extensions

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * setContentView()の呼び出しと、lifecycleOwnerセットをする
 */
fun <T : ViewDataBinding> AppCompatActivity.dataBinding(@LayoutRes layoutResId: Int): Lazy<T> {
    return object : Lazy<T> {
        private var binding: T? = null

        override fun isInitialized(): Boolean = binding != null

        override val value: T
            get() = binding ?: DataBindingUtil.inflate<T>(
                layoutInflater,
                layoutResId,
                null,
                false
            ).also {
                binding = it
                setContentView(it.root)
                it.lifecycleOwner = this@dataBinding
            }
    }
}
