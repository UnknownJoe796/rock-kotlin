package com.ivieleague.rock

/**
 * Extension functions for calls
 * Created by josep on 4/22/2017.
 */

fun MutableMap<String, Call>.merge(other: Map<String, Call>) {
    for ((key, value) in other) {
        val existing = this[key]
        if (existing == null) {
            this[key] = StandardCall(value)
        } else if (existing is StandardCall) {
            existing.merge(value)
        } else {
            val new = StandardCall(existing)
            new.merge(value)
            this[key] = new
        }
    }
}
