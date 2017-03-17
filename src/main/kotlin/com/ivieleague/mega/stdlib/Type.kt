package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder
import com.ivieleague.mega.CallRealization
import com.ivieleague.mega.get

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardType() {
    "type" - abstract {
        "equal" - interpretation {
            typesEqual(it.get("left"), it.get("right"))
        }
        "set" - interpretation {
            val interpretedPointer = it.copy().mutateContext().mutateKey("this").mutateInvoke() as InterpretedPointer
            interpretedPointer.value = it.mutateContext().mutateKey("value").mutateInvoke()
            interpretedPointer.value
        }
    }
}

fun typesEqual(first: CallRealization, second: CallRealization): Boolean {
    val firstUuid = first.mutateKey("uuid").mutateInvoke()
    val secondUuid = second.mutateKey("uuid").mutateInvoke()
    if (firstUuid == secondUuid) return true
    println("$firstUuid vs $secondUuid")
    return false
}