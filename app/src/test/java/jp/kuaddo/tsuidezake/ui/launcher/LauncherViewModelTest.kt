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

    describe("canStart") {
        it("should return true after 2 seconds.") {
            every { isAccountInitializedUseCase.invoke() } returns flowOf(true)
            val isInitializedObserver = viewModel.canStart.observeAndGet()

            testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)

            verify(exactly = 1) { isInitializedObserver.onChanged(true) }
        }

        it("should return true after authService is initialized") {
            val totalDelay = 3000L
            every { isAccountInitializedUseCase.invoke() } returns flow {
                emit(false)
                delay(totalDelay)
                emit(true)
            }
            val isInitializedObserver = viewModel.canStart.observeAndGet()

            testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)
            verify(exactly = 1) { isInitializedObserver.onChanged(false) }
            verify(exactly = 0) { isInitializedObserver.onChanged(true) }

            testDispatcher.advanceTimeBy(totalDelay - LauncherViewModel.INITIAL_DELAY)
            verifySequence {
                isInitializedObserver.onChanged(false)
                isInitializedObserver.onChanged(true)
            }
        }

        it("should return true after time out") {
            every { isAccountInitializedUseCase.invoke() } returns flowOf(false)
            val isInitializedObserver = viewModel.canStart.observeAndGet()

            testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)
            verify(exactly = 1) { isInitializedObserver.onChanged(false) }
            verify(exactly = 0) { isInitializedObserver.onChanged(true) }

            testDispatcher.advanceTimeBy(
                LauncherViewModel.TIME_OUT_DURATION - LauncherViewModel.INITIAL_DELAY
            )
            verifySequence {
                isInitializedObserver.onChanged(false)
                isInitializedObserver.onChanged(true)
            }
        }
    }
})
