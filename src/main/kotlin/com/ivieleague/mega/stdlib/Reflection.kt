package com.ivieleague.mega.stdlib

import com.ivieleague.mega.StandardFunction

fun StandardLibrary.reflection() {
    functions["mega.reflection.key"] = StandardFunction { it.quickResolveKey("value").key() }
    functions["mega.reflection.literal"] = StandardFunction { it.quickResolveKey("value").call().literal }
}