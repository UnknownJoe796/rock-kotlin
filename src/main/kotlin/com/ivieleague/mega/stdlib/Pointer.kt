package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder

/**
 * \n * Created by josep on 2/23/2017.
 */
interface InterpretedPointer {
    var value: Any?
}

fun AbstractCallBuilder.standardPointer() {
    "pointer" - abstract {
        "get" - interpretation {
            val interpretedPointer = it.mutateContext().mutateKey("this").mutateInvoke() as InterpretedPointer
            interpretedPointer.value
        }
    }
}