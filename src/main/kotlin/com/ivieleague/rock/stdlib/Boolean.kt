package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction
import com.ivieleague.rock.builder.execute
import com.ivieleague.rock.builder.executeSequence

fun StandardLibrary.boolean() {
    functions["rock.boolean.true"] = StandardFunction { true }
    functions["rock.boolean.false"] = StandardFunction { false }

    functions["rock.boolean.and"] = StandardFunction {
        (it.execute("left") as kotlin.Boolean).and(it.execute("right") as kotlin.Boolean)
    }
    functions["rock.boolean.not"] = StandardFunction {
        (it.execute("value") as kotlin.Boolean).not()
    }
    functions["rock.boolean.or"] = StandardFunction {
        (it.execute("left") as kotlin.Boolean).or(it.execute("right") as kotlin.Boolean)
    }
    functions["rock.boolean.xor"] = StandardFunction {
        (it.execute("left") as kotlin.Boolean).xor(it.execute("right") as kotlin.Boolean)
    }
    functions["rock.boolean.equals"] = StandardFunction {
        (it.execute("left") as kotlin.Any) == it.execute("right") as kotlin.Any
    }

    functions["rock.boolean.all"] = StandardFunction { it.executeSequence<Boolean>("values").all { it } }
    functions["rock.boolean.any"] = StandardFunction { it.executeSequence<Boolean>("values").any { it } }
    functions["rock.boolean.xorList"] = StandardFunction { it.executeSequence<Boolean>("values").fold(false) { current, it -> current xor it } }
}