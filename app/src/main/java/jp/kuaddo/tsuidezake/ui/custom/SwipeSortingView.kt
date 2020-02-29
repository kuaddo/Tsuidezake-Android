package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.material.chip.Chip
import jp.kuaddo.tsuidezake.databinding.ViewRecommendDrinkBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class SwipeSortingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layoutInflater = LayoutInflater.from(context)
    private val bindingQueue: Queue<ViewRecommendDrinkBinding> = ArrayBlockingQueue(2)
    private var nextDrinkIndex: Int = 0

    private var drinks: List<DrinkDetail>? = null
    var onNextDrinkShown: ((DrinkDetail) -> Unit)? = null
    var onLastDrinkRemoved: (() -> Unit)? = null

    fun submitDrinks(drinks: List<DrinkDetail>) {
        this.drinks = drinks

        if (drinks.isNotEmpty()) initializeRecommendDrinkBinding(drinks[0])
        if (drinks.size > 1) initializeRecommendDrinkBinding(drinks[1])
        nextDrinkIndex = minOf(2, drinks.size)

        setOnClickListener { replaceFront() }
    }

    private fun initializeRecommendDrinkBinding(drinkDetail: DrinkDetail) {
        val binding = ViewRecommendDrinkBinding.inflate(layoutInflater).also {
            it.drinkDetail = drinkDetail
            it.tagsChipGroup.let { chipGroup ->
                drinkDetail.tags.map { Chip(context).apply { text = it } }
                    .forEach { chip -> chipGroup.addView(chip) }
            }
            it.executePendingBindings()
        }
        bindingQueue.offer(binding)
        addView(binding.root, BACK)
    }

    private fun replaceFront() {
        val drinks = drinks ?: return
        when {
            nextDrinkIndex > drinks.size -> {
                removeViewAt(BACK)
                onLastDrinkRemoved?.invoke()
                return
            }
            nextDrinkIndex == drinks.size -> {
                removeViewAt(FRONT)
                nextDrinkIndex++
                return
            }
        }

        removeViewAt(FRONT)
        val binding = bindingQueue.poll()?.also {
            it.drinkDetail = drinks[nextDrinkIndex++]
            it.executePendingBindings()
        } ?: return
        bindingQueue.offer(binding)
        addView(binding.root, BACK)
    }

    companion object {
        private const val BACK = 0
        private const val FRONT = 1
    }
}