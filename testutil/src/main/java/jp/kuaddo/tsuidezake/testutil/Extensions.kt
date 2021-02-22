package jp.kuaddo.tsuidezake.testutil

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.dsl.GroupBody

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun GroupBody.applyArchTaskExecutor() {

    beforeEachTest { ArchTaskExecutor.getInstance().setDelegate(TestArchTaskExecutor()) }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
@ExperimentalCoroutinesApi
fun GroupBody.applyTestDispatcher(dispatcher: CoroutineDispatcher = Dispatchers.Unconfined) {
    beforeEachTest { Dispatchers.setMain(dispatcher) }

    afterEachTest { Dispatchers.resetMain() }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.observeAndGet() = mockk<Observer<T>>(relaxed = true).also { observeForever(it) }
