package jp.kuaddo.tsuidezake.core

suspend inline fun <T> T.alsoS(crossinline block: suspend (T) -> Unit): T = also { block(this) }
