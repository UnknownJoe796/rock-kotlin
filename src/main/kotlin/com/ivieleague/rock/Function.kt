package com.ivieleague.rock

import com.ivieleague.generic.equalsMap
import com.ivieleague.generic.hashCodeMap

interface Function {
    val inheritsFrom: String?
    val interpretation: ((InterpretationInterface) -> Any?)?
    val executions: Map<String, Reference>
    val arguments: Map<String, Reference>
}

fun Function.extEquals(other: Function): Boolean = executions.equalsMap(other.executions) && arguments.equalsMap(other.arguments)
fun Function.extHashCode(): Int = (executions.hashCodeMap() shl 2) xor arguments.hashCodeMap()

fun Function.getExecution(root: Root, key: String): Reference? {
    return executions[key] ?: root.getExecution(inheritsFrom ?: return null, key)
}

fun Function.getArgument(root: Root, key: String): Reference? {
    return arguments[key] ?: root.getArgument(inheritsFrom ?: return null, key)
}

tailrec fun Root.getExecution(function: String, key: String): Reference? {
    val func = functions[function] ?: throw IllegalArgumentException("Root has no function $function.")
    return func.executions[key] ?: getExecution(func.inheritsFrom ?: return null, key)
}

tailrec fun Root.getArgument(function: String, key: String): Reference? {
    val func = functions[function] ?: throw IllegalArgumentException("Root has no function $function.")
    return func.arguments[key] ?: getArgument(func.inheritsFrom ?: return null, key)
}