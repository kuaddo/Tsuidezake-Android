package jp.kuaddo.tsuidezake.ui.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AutoScroller(
    private val lifecycleOwner: LifecycleOwner,
    private val durationMillis: Long,
    private val viewPager: ViewPager2,
    private val getJumpPosition: (position: Int) -> Int?
) : LifecycleObserver {
    private val infinityScrollCallback = object : ViewPager2.OnPageChangeCallback() {
        private var newPosition: Int? = null
        private var isDragging = false

        override fun onPageSelected(position: Int) {
            currentPosition = position
            newPosition = getJumpPosition(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    newPosition?.let { position ->
                        viewPager.setCurrentItem(position, false)
                        newPosition = null
                    }
                    if (isDragging) {
                        isDragging = false
                        startAutoScroll()
                    }
                }
                ViewPager2.SCROLL_STATE_DRAGGING -> {
                    isDragging = true
                    stopAutoScroll()
                }
                ViewPager2.SCROLL_STATE_SETTLING -> Unit
            }
        }
    }

    private var currentPosition = 0
    private var scrollJob: Job? = null

    init {
        viewPager.registerOnPageChangeCallback(infinityScrollCallback)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun startAutoScroll() {
        scrollJob?.cancel()
        scrollJob = lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                delay(durationMillis)
                viewPager.setCurrentItem(currentPosition + 1, true)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stopAutoScroll() = scrollJob?.cancel()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        viewPager.unregisterOnPageChangeCallback(infinityScrollCallback)
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}
