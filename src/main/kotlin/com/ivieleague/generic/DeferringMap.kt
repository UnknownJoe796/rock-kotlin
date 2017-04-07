package com.ivieleague.generic

/**
 * Created by joseph on 4/6/17.
 */
class DeferringMap<K, V>(val maps: Sequence<Map<K, V>>) : Map<K, V> {
    override val entries: Set<Map.Entry<K, V>>
        get() = maps.flatMap { it.entries.asSequence() }.distinctBy { it.key }.toSet()
    override val keys: Set<K>
        get() = maps.flatMap { it.keys.asSequence() }.toSet()
    override val size: Int
        get() = maps.flatMap { it.keys.asSequence() }.distinct().count()
    override val values: Collection<V>
        get() = maps.flatMap { it.values.asSequence() }.toList()

    override fun containsKey(key: K): Boolean = maps.any { it.containsKey(key) }
    override fun containsValue(value: V): Boolean = maps.any { it.containsValue(value) }
    override fun get(key: K): V? {
        return maps.asSequence().map { it[key] }.firstOrNull()
    }

    override fun isEmpty(): Boolean = maps.all { it.isEmpty() }
}