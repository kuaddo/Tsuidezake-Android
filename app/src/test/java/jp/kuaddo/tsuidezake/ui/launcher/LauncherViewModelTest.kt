package jp.kuaddo.tsuidezake.ui.launcher

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import jp.kuaddo.tsuidezake.applyArchTaskExecutor
import jp.kuaddo.tsuidezake.applyTestDispatcher
import jp.kuaddo.tsuidezake.domain.IsAccountInitializedUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.observeAndGet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
object LauncherViewModelTest : Spek({
    val testDispatcher = TestCoroutineDispatcher()

    applyArchTaskExecutor()
    applyTestDispatcher(testDispatcher)

    val isAccountInitializedUseCase by memoized { mockk<IsAccountInitializedUseCase>() }
    val viewModel by memoized {
        LauncherViewModel(
            isAccountInitializedUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

    describe("isInitialized") {
        it("should return true after 2 seconds.") {
            every { isAccountInitializedUseCase.invoke() } returns flowOf(true)
            val isInitializedObserver = viewModel.isInitialized.observeAndGet()

            testDispatcher.advanceTimeBy(2000)

            verify(exactly = 1) {
                isInitializedObserver.onChanged(true)
            }
        }

        it("should return true after authService is initialized") {
            every { isAccountInitializedUseCase.invoke() } returns flow {
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
