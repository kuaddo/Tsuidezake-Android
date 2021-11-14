package jp.kuaddo.tsuidezake.core

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRegistrationKeyCreator @Inject constructor() {
    private val counter = AtomicInteger(0)

    fun generateKey() = "ActivityRegistrationKey${counter.getAndIncrement()}"
}
