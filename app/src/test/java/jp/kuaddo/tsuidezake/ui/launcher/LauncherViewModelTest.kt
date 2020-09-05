package jp.kuaddo.tsuidezake.ui.launcher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import jp.kuaddo.tsuidezake.applyArchTaskExecutor
import jp.kuaddo.tsuidezake.applyTestDispatcher
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.observeAndGet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
object LauncherViewModelTest : Spek({

    val testDispatcher = TestCoroutineDispatcher()

    applyArchTaskExecutor()
    applyTestDispatcher(testDispatcher)

    val authService by memoized { mockk<AuthService>() }
    val viewModel by memoized {
        LauncherViewModel(
            authService,
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

    describe("isInitialized") {
        it("should return true after 2 seconds.") {
            every { authService.initialized } returns MutableLiveData(true)
            val isInitializedObserver = viewModel.isInitialized.observeAndGet()

            testDispatcher.advanceTimeBy(2000)

            verify(exactly = 1) {
                isInitializedObserver.onChanged(true)
            }
        }

        it("should return true after authService is initialized") {
            every { authService.initialized } returns liveData {
                emit(false)
                delay(3000)
                emit(true)
            }
            val isInitializedObserver = viewModel.isInitialized.observeAndGet()

            testDispatcher.advanceTimeBy(2000)
            verify(exactly = 1) {
                isInitializedObserver.onChanged(false)
            }
            verify(exactly = 0) {
                isInitializedObserver.onChanged(true)
            }

            testDispatcher.advanceTimeBy(1000)
            verifySequence {
                isInitializedObserver.onChanged(false)
                isInitializedObserver.onChanged(true)
            }
        }
    }
})
