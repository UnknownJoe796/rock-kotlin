package com.ivieleague.generic

/**
 * \n * Created by josep on 12/24/2016.
 */
interface OrderedMap<K, V> : Iterable<Pair<K, V>> {
    override fun iterator(): Iterator<Pair<K, V>>

    val size: Int

    operator fun get(key: K): V?
    operator fun set(key: K, value: V)

    operator fun get(index: Int): V
    operator fun set(index: Int, value: V)
}