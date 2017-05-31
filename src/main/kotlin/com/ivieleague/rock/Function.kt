package com.ivieleague.rock

import com.ivieleague.generic.equalsMap
import com.ivieleague.generic.hashCodeMap

interface Function {
    val interpretation: ((InterpretationInterface) -> Any?)?
    val executions: Map<String, Call>
    val arguments: Map<String, Reference>
}

fun Function.extEquals(other: Function): Boolean = executions.equalsMap(other.executions) && arguments.equalsMap(other.arguments)
fun Function.extHashCode(): Int = (executions.hashCodeMap() shl 2) xor arguments.hashCodeMap()