package jp.kuaddo.tsuidezake.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ViewRecommendSakeBinding
import jp.kuaddo.tsuidezake.model.SakeDetail
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.cos
import kotlin.math.sin

class SwipeSortingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val layoutInflater = LayoutInflater.from(context)
    private val closeButton: ImageView
    private val cardContainer: FrameLayout
    private val bindingQueue: Queue<ViewRecommendSakeBinding> = ArrayBlockingQueue(2)
    private var nextSakeIndex: Int = 0

    private var sakes: List<SakeDetail>? = null
    var onLastSakeRemoved: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.view_swipe_sorting, this)
        closeButton = findViewById(R.id.close_button)
        cardContainer = findViewById(R.id.card_container)

        closeButton.setOnClickListener { onLastSakeRemoved?.invoke() }
    }

    fun submitSakes(sakes: List<SakeDetail>) {
        this.sakes = sakes
        if (sakes.isEmpty()) return

        initializeRecommendSakeBinding(sakes[0])
        if (sakes.size > 1) initializeRecommendSakeBinding(sakes[1])
        setOnTouchListener(FRONT)
        nextSakeIndex = minOf(2, sakes.size)
    }

    private fun replaceFront() {
        val sakes = sakes ?: return
        when {
            nextSakeIndex > sakes.size -> {
                resetViewState(BACK)
                onLastSakeRemoved?.invoke()
                return
            }
            nextSakeIndex == sakes.size -> {
                resetViewState(FRONT)
                setOnTouchListener(BACK)
                nextSakeIndex++
                return
            }
        }

        resetViewState(FRONT)
        setOnTouchListener(BACK)
        bindingQueue.poll()?.apply {
            update(sakes[nextSakeIndex++])
            insertBinding(this)
        }
    }

    private fun initializeRecommendSakeBinding(sakeDetail: SakeDetail) {
        ViewRecommendSakeBinding.inflate(layoutInflater).apply {
            update(sakeDetail)
            insertBinding(this)
        }
    }

    private fun setOnTouchListener(index: Int) {
        val targetView = cardContainer.getChildAt(index)
        val wishIcon = targetView.findViewById<View>(R.id.wish_icon)
        val doNotWishIcon = targetView.findViewById<View>(R.id.do_not_wish_icon)
        targetView.setOnTouchListener(
            MovingAndRotationTouchListener(wishIcon, doNotWishIcon)
        )
    }

    private fun insertBinding(binding: ViewRecommendSakeBinding) {
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

    private fun ViewRecommendSakeBinding.update(sakeDetail: SakeDetail) {
        this.sakeDetail = sakeDetail
        tagsChipGroup.let { chipGroup ->
            chipGroup.removeAllViews()
            sakeDetail.tags.map { Chip(context).apply { text = it } }
                .forEach { chip -> chipGroup.addView(chip) }
        }
        doNotWishButton.setOnClickListener { replaceFront() }
        wishButton.setOnClickListener { replaceFront() }
        executePendingBindings()
    }

    inner class MovingAndRotationTouchListener(
        private val wishIcon: View,
        private val doNotWishIcon: View,
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
                        (event.y - height / 2) * sin(Math.toRadians(v.rotation.toDouble())) +
                        width / 2
                    val moveDistance = x - initialEventX + v.x - initialX
                    v.x = moveDistance.toFloat() * movingScale
                    v.rotation = moveDistance.toFloat() * rotationScale
                    setRatioToIcon(getMoveDistanceRatio(v))
                }
                MotionEvent.ACTION_UP -> {
                    v.performClick()
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
            wishIcon.alpha = maxOf(0f, minOf(ratio, 1f))
            doNotWishIcon.alpha = maxOf(0f, minOf(-ratio, 1f))
        }
    }

    companion object {
        private const val BACK = 0
        private const val FRONT = 1
    }
}
