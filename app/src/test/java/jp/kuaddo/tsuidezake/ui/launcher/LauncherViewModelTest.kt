package jp.kuaddo.tsuidezake.ui.launcher

import io.mockk.every
import io.mockk.mockk
import jp.kuaddo.tsuidezake.domain.IsAccountInitializedUseCase
import jp.kuaddo.tsuidezake.domain.invoke
import jp.kuaddo.tsuidezake.testutil.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LauncherViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val coroutineRule = CoroutineTestRule(testDispatcher)

    private lateinit var isAccountInitializedFlow: MutableSharedFlow<Boolean>
    private lateinit var target: LauncherViewModel

    @Before
    fun setUp() {
        isAccountInitializedFlow = MutableSharedFlow()
        val isAccountInitializedUseCase = mockk<IsAccountInitializedUseCase> {
            every { this@mockk.invoke() } returns isAccountInitializedFlow
        }
        target = LauncherViewModel(
            isAccountInitializedUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

    @Test
    fun testCanStart_returnsTrueAfter2Seconds() = testDispatcher.runBlockingTest {
        isAccountInitializedFlow.emit(true)
        assertThat(target.canStart.first()).isFalse

        testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)

        assertThat(target.canStart.first()).isTrue
    }

    @Test
    fun testCanStart_returnsTrueAfterAuthServiceInitialization() = testDispatcher.runBlockingTest {
        val additionalDelay = 1000L
        launch {
            isAccountInitializedFlow.emit(false)
            delay(LauncherViewModel.INITIAL_DELAY + additionalDelay)
            isAccountInitializedFlow.emit(true)
        }
        assertThat(target.canStart.first()).isFalse

        testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)
        assertThat(target.canStart.first()).isFalse
        testDispatcher.advanceTimeBy(additionalDelay)
        assertThat(target.canStart.first()).isTrue
    }

    @Test
    fun testCanStart_returnsTrueAfterTimeout() = testDispatcher.runBlockingTest {
        isAccountInitializedFlow.emit(false)
        assertThat(target.canStart.first()).isFalse

        testDispatcher.advanceTimeBy(LauncherViewModel.INITIAL_DELAY)
        assertThat(target.canStart.first()).isFalse
        testDispatcher.advanceTimeBy(
            LauncherViewModel.TIME_OUT_DURATION - LauncherViewModel.INITIAL_DELAY
        )
        assertThat(target.canStart.first()).isTrue
    }
}
