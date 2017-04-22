package com.ivieleague.mega

/**
 * Extension functions for references
 * Created by josep on 4/22/2017.
 */

fun Reference.merge(other: Reference): Reference {
    return if (this is Reference.RCall && other is Reference.RCall) {
        val call = this.call
        if (call is StandardCall) {
            call.merge(other.call)
            this
        } else {
            val new = StandardCall(call)
            new.merge(other.call)
            Reference.RCall(new)
        }
    } else {
        this
    }
}

fun MutableMap<String, Reference>.merge(other: Map<String, Reference>) {
    for ((key, value) in other) {
        val existing = this[key]
        if (existing == null) this[key] = value
        else this[key] = existing.merge(value)
    }
}