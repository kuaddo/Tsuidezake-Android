package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import jp.kuaddo.tsuidezake.R

class ThermometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.view_thermometer, this)
    }
}
