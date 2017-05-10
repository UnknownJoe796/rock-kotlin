package com.ivieleague.generic

/**
 * Created by josep on 5/10/2017.
 */

fun <T> List<T>.equalsList(other: List<T>): Boolean {
    if (this.size != other.size) return false
    val iterA = iterator()
    val iterB = other.iterator()
    while (iterA.hasNext()) {
        if (iterA.next() != iterB.next()) return false
    }
    return true
}

fun <T : Any> List<T>.hashCodeList(): Int {
    var current = 0
    for (item in this) {
        current = (current shl 1) xor item.hashCode()
    }
    return current
}

fun <K : Any, V : Any> Map<K, V>.equalsMap(other: Map<K, V>): Boolean {
    if (this.size != other.size) return false
    for (key in keys) {
        if (this[key] != other[key]) return false
    }
    return true
}

fun <K : Any, V : Any> Map<K, V>.hashCodeMap(): Int {
    var current = 0
    for ((key, value) in this) {
        current = (current shl 2) xor (key.hashCode() shl 1) xor (value.hashCode())
    }
    return current
}