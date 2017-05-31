package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction

fun StandardLibrary.void() {
    functions["rock.void.literal"] = StandardFunction { }
}