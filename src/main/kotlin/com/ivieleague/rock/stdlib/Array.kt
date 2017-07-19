package com.ivieleague.rock.stdlib

import com.ivieleague.rock.*
import com.ivieleague.rock.builder.execute
import com.ivieleague.rock.builder.executeSequence

fun StandardLibrary.array() {
    functions["rock.array.literal"] = StandardFunction { it.executeSequence<Any?>("values").toList().toTypedArray() }
    functions["rock.array.copies"] = StandardFunction {
        val value = it.execute("value")
        Array<Any?>(it.execute("size") as Int, { value })
    }
    val indicies = HashMap<InterpretationInterface, Int>()
    functions["rock.array.build"] = StandardFunction {
        Array<Any?>(it.execute("size") as Int, { index ->
            indicies[it] = index
            it.execute("value")
        })
    }.apply {
        arguments["index"] = Reference.RVirtualCall({ StandardCall(PrimitiveConversion.LITERAL_INTEGER, literal = indicies[it]) })
    }
    functions["rock.array.pointer"] = StandardFunction {
        val array = it.execute("array") as Array<Any?>
        val index = it.execute("index") as Int
        object : InterpretedPointer {
            override var value: Any?
                get() = array[index]
                set(value) {
                    array[index] = value
                }
        }
    }
    functions["rock.array.append"] = StandardFunction {
        val left = (it.execute("left") as Array<Any?>)
        val right = ((it.execute("right") as Array<Any?>))

        val result = java.util.Arrays.copyOf(left, left.size + right.size)
        System.arraycopy(right, 0, result, left.size, right.size)
    }
    functions["rock.array.appendList"] = StandardFunction {
        it.executeSequence<Array<Any?>>("arrays").flatMap { it.asSequence() }.toList().toTypedArray()
    }
}