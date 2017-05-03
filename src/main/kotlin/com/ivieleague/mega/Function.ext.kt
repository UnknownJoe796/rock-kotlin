package com.ivieleague.mega

/**
 * Extension functions for calls
 * Created by josep on 4/22/2017.
 */

fun MutableMap<String, Function>.merge(other: Map<String, Function>) {
    for ((key, value) in other) {
        val existing = this[key]
        if (existing == null) {
            this[key] = StandardFunction(value)
        } else if (existing is StandardFunction) {
            existing.merge(value)
        } else {
            val new = StandardFunction(existing)
            new.merge(value)
            this[key] = new
        }
    }
}
