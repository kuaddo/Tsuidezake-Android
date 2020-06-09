package jp.kuaddo.tsuidezake.core.live

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.UUID

/**
 * @param generateUUIDTag
 *  MediatorLiveData.addSource()等を利用する場合にはobserve(owner, "", observer)が呼ばれてしまう。
 *  tagが""で同じになるので、複数のObserverに通知ができない。generateUUIDTagをtrueにすることで
 *  この問題を解決することができる。
 */
open class LiveEvent<T>(private val generateUUIDTag: Boolean = false) : MutableLiveData<T>() {
    private val dispatchedTagSet = mutableSetOf<String>()

    @MainThread
    @Deprecated(
        message = "Multiple observers registered but only one will be notified of changes. " +
            "set tags for each observer.",
        replaceWith = ReplaceWith("observe(owner, \"\", observer)"),
        level = DeprecationLevel.HIDDEN
    )
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observe(owner, if (generateUUIDTag) UUID.randomUUID().toString() else "", observer)
    }

    @MainThread
    @Deprecated(
        message = "Multiple observers registered but only one will be notified of changes. " +
            "set tags for each observer.",
        replaceWith = ReplaceWith("observeForever(\"\", observer)"),
        level = DeprecationLevel.HIDDEN
    )
    override fun observeForever(observer: Observer<in T>) {
        observeForever(if (generateUUIDTag) UUID.randomUUID().toString() else "", observer)
    }

    @MainThread
    open fun observe(owner: LifecycleOwner, tag: String, observer: Observer<in T>) {
        super.observe(owner, Observer {
            val internalTag = owner::class.java.name + "#" + tag
            if (!dispatchedTagSet.contains(internalTag)) {
                dispatchedTagSet.add(internalTag)
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    open fun observeForever(tag: String, observer: Observer<in T>) {
        super.observeForever {
            if (!dispatchedTagSet.contains(tag)) {
                dispatchedTagSet.add(tag)
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        dispatchedTagSet.clear()
        super.setValue(t)
    }
}
