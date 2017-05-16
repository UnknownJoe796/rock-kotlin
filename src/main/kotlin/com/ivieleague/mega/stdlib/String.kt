package com.ivieleague.mega.stdlib

import com.ivieleague.mega.StandardFunction
import com.ivieleague.mega.builder.execute
import com.ivieleague.mega.builder.executeSequence


fun StandardLibrary.string() {
    functions["mega.string.literal"] = StandardFunction { it.call().literal }
    functions["mega.string.concatenate"] = StandardFunction {
        (it.execute("left") as String).plus(it.execute("right") as String)
    }
    functions["mega.string.concatenateList"] = StandardFunction { it.executeSequence<String>("values").joinToString("") }
    functions["mega.string.join"] = StandardFunction { it.executeSequence<String>("values").joinToString(it.execute("separator") as String) }
}