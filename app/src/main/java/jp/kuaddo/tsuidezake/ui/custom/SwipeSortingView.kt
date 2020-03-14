package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
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
        bindingQueue.poll()?.apply {
            update(drinks[nextDrinkIndex++])
            insertBinding(this)
        }
    }

    private fun initializeRecommendDrinkBinding(drinkDetail: DrinkDetail) {
        ViewRecommendDrinkBinding.inflate(layoutInflater).apply {
            update(drinkDetail)
            insertBinding(this)
        }
    }

    private fun setOnTouchListener(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        val wantToDrink = targetView.findViewById<View>(R.id.want_to_drink_icon)
        val doNotWantToDrink = targetView.findViewById<View>(R.id.do_not_want_to_drink_icon)
        targetView.setOnTouchListener(
            MovingAndRotationTouchListener(wantToDrink, doNotWantToDrink)
        )
    }

    private fun insertBinding(binding: ViewRecommendDrinkBinding) {
        bindingQueue.offer(binding)
        cardContainer.addView(binding.root, BACK)
    }

    private fun resetViewState(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        targetView.x = 0f
        targetView.rotation = 0f
        targetView.setOnTouchListener(null)
        cardContainer.removeViewAt(index)
    }

    private fun ViewRecommendDrinkBinding.update(drinkDetail: DrinkDetail) {
        this.drinkDetail = drinkDetail
        tagsChipGroup.let { chipGroup ->
            chipGroup.removeAllViews()
            drinkDetail.tags.map { Chip(context).apply { text = it } }
                .forEach { chip -> chipGroup.addView(chip) }
        }
        doNotWantToDrinkButton.setOnClickListener { replaceFront() }
        wantToDrinkButton.setOnClickListener { replaceFront() }
        executePendingBindings()
    }

    inner class MovingAndRotationTouchListener(
        private val wantToDrinkIcon: View,
        private val doNotWantToDrinkIcon: View,
        private val movingScale: Float = 1.2f,
        private val rotationScale: Float = 0.03f,
        private val downOffsetDp: Int = 10,
        private val alpha: Float = 0.8f
    ) : OnTouchListener {
        private val density = resources.displayMetrics.density
        private var initialX: Float = 0f
        private var initialEventX: Float = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = v.x
                    initialEventX = event.x
                    v.y += downOffsetDp * density
                    v.alpha = alpha
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = (event.x - width / 2) * cos(Math.toRadians(v.rotation.toDouble())) -
                        (event.y - height / 2) * sin(Math.toRadians(v.rotation.toDouble())) + width / 2
                    val moveDistance = x - initialEventX + v.x - initialX
                    v.x = moveDistance.toFloat() * movingScale
                    v.rotation = moveDistance.toFloat() * rotationScale
                    setRatioToIcon(getMoveDistanceRatio(v))
                }
                MotionEvent.ACTION_UP -> {
                    v.y -= downOffsetDp * density
                    v.alpha = 1.0f
                    setRatioToIcon(0f)

                    val moveDistance = (v.x - initialX).toInt()
                    if (moveDistance in -(width / 3)..(width / 3)) {
                        v.x = 0f
                        v.rotation = 0f
                    } else replaceFront()
                }
            }
            return true
        }

        private fun getMoveDistanceRatio(v: View): Float {
            val moveDistance = (v.x - initialX)
            return maxOf(-1f, minOf(moveDistance * 3 / width, 1f))
        }

        private fun setRatioToIcon(ratio: Float) {
            require(ratio in -1f..1f)
            wantToDrinkIcon.alpha = maxOf(0f, minOf(ratio, 1f))
            doNotWantToDrinkIcon.alpha = maxOf(0f, minOf(-ratio, 1f))
        }
    }

    companion object {
        private const val BACK = 0
        private const val FRONT = 1
    }
}