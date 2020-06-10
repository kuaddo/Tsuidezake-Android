package jp.kuaddo.tsuidezake.core.live

import androidx.annotation.MainThread

/**
 * @param generateUUIDTag [LiveEvent]のドキュメントに詳細を記述
 */
open class UnitLiveEvent(generateUUIDTag: Boolean = false) : LiveEvent<Unit>(generateUUIDTag) {
    fun call() {
        super.setValue(Unit)
    }

    fun callFromWorkerThread() {
        postValue(Unit)
    }

    @MainThread
    @Deprecated(
        message = "use call()",
        replaceWith = ReplaceWith("call()"),
        level = DeprecationLevel.HIDDEN
    )
    override fun setValue(t: Unit?) {
        super.setValue(t)
    }
}
