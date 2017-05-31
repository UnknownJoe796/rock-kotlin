package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction

fun StandardLibrary.reflection() {
    functions["rock.reflection.key"] = StandardFunction { it.quickResolveKey("value").key() }
    functions["rock.reflection.literal"] = StandardFunction { it.quickResolveKey("value").call().literal }
}