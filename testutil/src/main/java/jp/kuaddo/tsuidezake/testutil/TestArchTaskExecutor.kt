package jp.kuaddo.tsuidezake.testutil

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.TaskExecutor

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
class TestArchTaskExecutor : TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) {
        runnable.run()
    }

    override fun isMainThread(): Boolean {
        return true
    }

    override fun postToMainThread(runnable: Runnable) {
        runnable.run()
    }
}
