package com.ivieleague.rock

import com.ivieleague.generic.equalsMap
import com.ivieleague.generic.hashCodeMap

/**
 * Alternative system.
 * Created by josep on 4/12/2017.
 */

interface Root {
    val functions: Map<String, Function>
    val calls: Map<String, Call>
}

fun Root.extEquals(other: Root): Boolean = functions.equalsMap(other.functions) && calls.equalsMap(other.calls)
fun Root.extHashCode(): Int = (functions.hashCodeMap() shl 2) xor calls.hashCodeMap()