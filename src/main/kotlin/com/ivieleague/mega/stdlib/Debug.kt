package com.ivieleague.mega.stdlib

import com.ivieleague.mega.StandardFunction
import com.ivieleague.mega.builder.execute


fun StandardLibrary.debug() {
    functions["mega.debug.print"] = StandardFunction { println(it.execute("value")) }
}