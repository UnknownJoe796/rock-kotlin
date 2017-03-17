package com.ivieleague.generic

import java.util.*

/**
 * \n * Created by josep on 12/24/2016.
 */
class HashArrayMap<K, V> : OrderedMap<K, V> {
    private val privateMap = HashMap<K, V>()
    private val privateList = ArrayList<Pair<K, V>>()

    override val size: Int
        get() = privateList.size

    override fun iterator(): Iterator<Pair<K, V>> = privateList.iterator()

    override operator fun get(key: K): V? = privateMap[key]
    override operator fun set(key: K, value: V) {
        val currentExists = privateMap.containsKey(key)
        if (!currentExists) {
            privateMap[key] = value
            privateList.add(key to value)
        } else {
            privateMap[key] = value
            privateList[privateList.indexOfFirst { it.first == key }] = key to value
        }
    }

    override operator fun get(index: Int): V = privateList[index].second
    override operator fun set(index: Int, value: V) {
        val key = privateList[index].first
        privateMap[key] = value
        privateList[privateList.indexOfFirst { it.first == key }] = key to value
    }
}