package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction

fun StandardLibrary.function() {
    functions["rock.function"] = StandardFunction {
        //TODO: make this use eager evaluation of arguments
        it.quickResolveKey("body").addedArguments(it.quickResolveKey("arguments")).execute()
    }
}