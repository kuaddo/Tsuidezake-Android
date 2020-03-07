package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import jp.kuaddo.tsuidezake.R

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this)

        setBackgroundResource(R.color.transparent_background)
        isClickable = true
        isFocusable = true
    }
}