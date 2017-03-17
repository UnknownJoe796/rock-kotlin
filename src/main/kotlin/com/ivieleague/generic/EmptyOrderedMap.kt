package com.ivieleague.generic

/**
 * \n * Created by josep on 12/24/2016.
 */
class EmptyOrderedMap<K, V> : OrderedMap<K, V> {
    override fun iterator(): Iterator<Pair<K, V>> = emptySequence<Pair<K, V>>().iterator()

    override val size: Int = 0

    override operator fun get(key: K): V? = null
    override operator fun set(key: K, value: V) = throw UnsupportedOperationException()

    override operator fun get(index: Int): V = throw UnsupportedOperationException()
    override operator fun set(index: Int, value: V) = throw UnsupportedOperationException()
}