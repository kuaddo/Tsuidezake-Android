package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.model.SuitableTemperature

class ThermometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val lines: List<View>
    private val dots: List<View>

    var suitableTemperatures: Set<SuitableTemperature> = emptySet()
        set(value) {
            hideLinesAndDots()
            value.forEach { temperature ->
                temperature.toLineIndex()?.let { index -> lines[index].isVisible = true }
                temperature.toDotIndexPair()?.let { (startIndex, endIndex) ->
                    dots[startIndex].isVisible = !dots[startIndex].isVisible
                    dots[endIndex].isVisible = !dots[endIndex].isVisible
                }
            }
            field = value
        }

    init {
        View.inflate(context, R.layout.view_thermometer, this)
        lines = listOf(
            findViewById(R.id.line1_2),
            findViewById(R.id.line2_3),
            findViewById(R.id.line3_4),
            findViewById(R.id.line4_5)
        )
        dots = listOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3),
            findViewById(R.id.dot4),
            findViewById(R.id.dot5)
        )
    }

    private fun hideLinesAndDots() {
        lines.forEach { it.isVisible = false }
        dots.forEach { it.isVisible = false }
    }

    private fun SuitableTemperature.toLineIndex(): Int? = listOf(
        SuitableTemperature.COLD,
        SuitableTemperature.NORMAL,
        SuitableTemperature.WARM,
        SuitableTemperature.HOT
    ).indexOf(this).takeIf { it != -1 }

    private fun SuitableTemperature.toDotIndexPair(): Pair<Int, Int>? =
        toLineIndex()?.let { it to it + 1 }
}

@BindingAdapter("suitableTemperatures")
fun ThermometerView.bindSuitableTemperatures(suitableTemperatures: Set<SuitableTemperature>?) {
    this.suitableTemperatures = suitableTemperatures ?: emptySet()
}
