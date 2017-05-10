package com.ivieleague.mega

/**
 * Standard root
 * Created by josep on 4/22/2017.
 */
class StandardRoot : Root {
    override val functions = HashMap<String, Function>()
    override val calls = HashMap<String, Call>()

    fun merge(other: Root) {
        functions.merge(other.functions)
        calls.merge(other.calls)
    }

    override fun equals(other: Any?): Boolean = other is Root && extEquals(other)
    override fun hashCode(): Int = extHashCode()
}