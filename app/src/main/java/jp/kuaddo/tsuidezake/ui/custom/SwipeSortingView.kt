package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.chip.Chip
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ViewRecommendDrinkBinding
import jp.kuaddo.tsuidezake.model.DrinkDetail
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.cos
import kotlin.math.sin

class SwipeSortingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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
        targetView.setOnTouchListener(MovingAndRotationTouchListener())
    }

    private fun resetViewState(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        targetView.x = 0f
        targetView.rotation = 0f
        targetView.setOnTouchListener(null)
        cardContainer.removeViewAt(index)
    }

    inner class MovingAndRotationTouchListener(
        private val movingScale: Float = 1.2f,
        private val rotationScale: Float = 0.03f
    ) : OnTouchListener {
        private var initialX: Float = 0f
        private var initialEventX: Float = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = v.x
                    initialEventX = event.x
                    v.y += 50f
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = (event.x - width / 2) * cos(Math.toRadians(v.rotation.toDouble())) -
                        (event.y - height / 2) * sin(Math.toRadians(v.rotation.toDouble())) + width / 2
                    val moveDistance = x - initialEventX + v.x - initialX
                    v.x = moveDistance.toFloat() * movingScale
                    v.rotation = moveDistance.toFloat() * rotationScale
                }
                MotionEvent.ACTION_UP -> {
                    v.y -= 50f
                    val moveDistance = (v.x - initialX).toInt()
                    if (moveDistance in -(width / 3)..(width / 3)) {
                        v.x = 0f
                        v.rotation = 0f
                    } else replaceFront()
                }
            }
            return true
        }
    }

    companion object {
        private const val BACK = 0
        private const val FRONT = 1
    }
}