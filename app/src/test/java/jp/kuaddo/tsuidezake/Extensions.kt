package jp.kuaddo.tsuidezake

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

fun GroupBody.applyArchTaskExecutor() {

    beforeEachTest { ArchTaskExecutor.getInstance().setDelegate(TestArchTaskExecutor()) }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }
}

@ExperimentalCoroutinesApi
fun GroupBody.applyTestDispatcher(dispatcher: CoroutineDispatcher = Dispatchers.Unconfined) {
    beforeEachTest { Dispatchers.setMain(dispatcher) }

    afterEachTest { Dispatchers.resetMain() }
}

fun <T> LiveData<T>.observeAndGet() = mockk<Observer<T>>(relaxed = true).also { observeForever(it) }