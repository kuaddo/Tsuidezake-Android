package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ViewRecommendDrinkBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

class SwipeSortingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val layoutInflater = LayoutInflater.from(context)
    private val cardContainer: FrameLayout
    private val bindingQueue: Queue<ViewRecommendDrinkBinding> = ArrayBlockingQueue(2)
    private var nextDrinkIndex: Int = 0

    private var drinks: List<DrinkDetail>? = null
    var onLastDrinkRemoved: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.view_swipe_sorting, this)
        cardContainer = findViewById(R.id.card_container)
        findViewById<View>(R.id.do_not_want_to_drink_button).setOnClickListener { replaceFront() }
        findViewById<View>(R.id.want_to_drink_button).setOnClickListener { replaceFront() }
    }

    fun submitDrinks(drinks: List<DrinkDetail>) {
        this.drinks = drinks
        if (drinks.isEmpty()) return

        initializeRecommendDrinkBinding(drinks[0])
        if (drinks.size > 1) initializeRecommendDrinkBinding(drinks[1])
        setOnTouchListener(FRONT)
        nextDrinkIndex = minOf(2, drinks.size)
    }

    private fun replaceFront() {
        val drinks = drinks ?: return
        when {
            nextDrinkIndex > drinks.size -> {
                resetViewState(BACK)
                onLastDrinkRemoved?.invoke()
                return
            }
            nextDrinkIndex == drinks.size -> {
                resetViewState(FRONT)
                setOnTouchListener(BACK)
                nextDrinkIndex++
                return
            }
        }

        resetViewState(FRONT)
        setOnTouchListener(BACK)
        val binding = bindingQueue.poll()?.also {
            it.drinkDetail = drinks[nextDrinkIndex++]
            it.executePendingBindings()
        } ?: return
        bindingQueue.offer(binding)
        cardContainer.addView(binding.root, BACK)
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
        cardContainer.addView(binding.root, BACK)
    }

    private fun setOnTouchListener(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        targetView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    v.x += event.x - v.width / 2
                }
                MotionEvent.ACTION_UP -> {
                    // TODO: 要修正
                    if (event.rawX.toInt() in (width / 3)..(width * 2 / 3)) v.x = 0f
                    else replaceFront()
                }
            }
            true
        }
    }

    private fun resetViewState(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        targetView.x = 0f
        targetView.setOnTouchListener(null)
        cardContainer.removeViewAt(index)
    }

    companion object {
        private const val BACK = 0
        private const val FRONT = 1
    }
}