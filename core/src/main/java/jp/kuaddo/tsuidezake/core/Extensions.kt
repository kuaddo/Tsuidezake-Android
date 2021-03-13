@file:OptIn(ExperimentalContracts::class)

package jp.kuaddo.tsuidezake.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

suspend inline fun <T> T.alsoS(crossinline block: suspend (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

suspend inline fun <T, R> T.letS(crossinline block: suspend (T) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block(this)
}
