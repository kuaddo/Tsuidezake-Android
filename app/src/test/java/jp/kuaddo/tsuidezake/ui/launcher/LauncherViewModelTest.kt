package jp.kuaddo.tsuidezake.ui.launcher

import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import jp.kuaddo.tsuidezake.applyArchTaskExecutor
import jp.kuaddo.tsuidezake.applyTestDispatcher
import jp.kuaddo.tsuidezake.observeAndGet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
object LauncherViewModelTest : Spek({

    val testDispatcher = TestCoroutineDispatcher()

    applyArchTaskExecutor()
    applyTestDispatcher(testDispatcher)

    val viewModel by memoized { LauncherViewModel(mockk(relaxed = true), mockk(relaxed = true)) }

    describe("isInitialized") {

        lateinit var isInitializedObserver: Observer<Boolean>

        beforeEachTest {
            isInitializedObserver = viewModel.isInitialized.observeAndGet()
            testDispatcher.advanceTimeBy(2000)
        }

        it("should return true") {
            verify(exactly = 1) {
                isInitializedObserver.onChanged(
                    withArg { assertThat(it).isTrue() }
                )
            }
        }
    }
})