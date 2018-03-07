package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction
import com.ivieleague.rock.builder.execute
import com.ivieleague.rock.builder.executeMapSequence
import com.ivieleague.rock.builder.executeSequence


fun StandardLibrary.string() {
    functions["rock.string.literal"] = StandardFunction { it.call().literal }
    functions["rock.string.concatenate"] = StandardFunction {
        (it.execute("left") as String).plus(it.execute("right") as String)
    }
    functions["rock.string.concatenateList"] = StandardFunction { it.executeSequence<String>("values").joinToString("") }
    functions["rock.string.join"] = StandardFunction { it.executeSequence<String>("values").joinToString(it.execute("separator") as String) }
    functions["rock.string.joinMapValues"] = StandardFunction { it.executeMapSequence<String>("values").joinToString(it.execute("separator") as String) { it.second } }
}