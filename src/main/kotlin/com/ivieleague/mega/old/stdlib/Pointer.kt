package com.ivieleague.mega.old.stdlib

import com.ivieleague.mega.old.AbstractCallBuilder

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