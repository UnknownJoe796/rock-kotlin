package com.ivieleague.mega.stdlib

import com.ivieleague.mega.StandardFunction
import com.ivieleague.mega.builder.execute

interface InterpretedPointer {
    var value: Any?
}

fun StandardLibrary.pointer() {
    functions["mega.pointer.get"] = StandardFunction { (it.execute("this") as InterpretedPointer).value }
    functions["mega.pointer.set"] = StandardFunction {
        val newValue = it.execute("value")
        (it.execute("this") as InterpretedPointer).value = newValue
        newValue
    }
}