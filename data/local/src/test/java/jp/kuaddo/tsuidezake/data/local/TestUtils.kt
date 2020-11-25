package jp.kuaddo.tsuidezake.data.local

fun <T> Collection<T>.powerSet(): Set<Set<T>> =
    powerSet(this, setOf(emptySet()))

private tailrec fun <T> powerSet(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
    if (left.isEmpty()) acc
    else powerSet(left.drop(1), acc + acc.map { it + left.first() })
