package com.ivieleague.mega.builder

import com.ivieleague.mega.Call
import com.ivieleague.mega.InterpretationInterface
import com.ivieleague.mega.Reference
import com.ivieleague.mega.SubRef

fun InterpretationInterface.execute(key: String) = execute(Reference.RArgument(listOf(SubRef.Key(key))))
fun InterpretationInterface.identify(key: String) = identify(Reference.RArgument(listOf(SubRef.Key(key))))

fun InterpretationInterface.executeArgument(vararg children: Any) = execute(Reference.RArgument(children.map {
    when (it) {
        is String -> SubRef.Key(it)
        is Int -> SubRef.Index(it)
        else -> throw IllegalArgumentException()
    }
}))

fun InterpretationInterface.identifyArgument(vararg children: Any) = identify(Reference.RArgument(children.map {
    when (it) {
        is String -> SubRef.Key(it)
        is Int -> SubRef.Index(it)
        else -> throw IllegalArgumentException()
    }
}))

fun Reference.Companion.label(label: String, vararg children: Any) = Reference.RLabel(label, children.map {
    when (it) {
        is String -> SubRef.Key(it)
        is Int -> SubRef.Index(it)
        else -> throw IllegalArgumentException()
    }
})

fun Reference.Companion.argument(vararg children: Any) = Reference.RArgument(children.map {
    when (it) {
        is String -> SubRef.Key(it)
        is Int -> SubRef.Index(it)
        else -> throw IllegalArgumentException()
    }
})

fun Call.toReference() = Reference.RCall(this)