package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction
import com.ivieleague.rock.builder.execute

interface InterpretedPointer {
    var value: Any?
}

fun StandardLibrary.pointer() {
    functions["rock.pointer.get"] = StandardFunction { (it.execute("this") as InterpretedPointer).value }
    functions["rock.pointer.set"] = StandardFunction {
        val newValue = it.execute("value")
        (it.execute("this") as InterpretedPointer).value = newValue
        newValue
    }
}