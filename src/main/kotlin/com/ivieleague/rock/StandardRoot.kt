package com.ivieleague.rock

/**
 * Standard root
 * Created by josep on 4/22/2017.
 */
class StandardRoot : Root {
    override val functions = HashMap<String, Function>()
    override val calls = HashMap<String, Call>()

    fun merge(other: Root): StandardRoot {
        functions.merge(other.functions)
        calls.merge(other.calls)
        return this
    }

    override fun equals(other: Any?): Boolean = other is Root && extEquals(other)
    override fun hashCode(): Int = extHashCode()
}