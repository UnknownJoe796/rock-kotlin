package com.ivieleague.rock.stdlib

import com.ivieleague.rock.*
import com.ivieleague.rock.builder.execute
import com.ivieleague.rock.builder.executeSequence

class LoopBreak(val interp: InterpretationInterface) : Throwable()
class LoopContinue(val interp: InterpretationInterface) : Throwable()

fun StandardLibrary.controls() {
    functions["rock.control.if"] = StandardFunction { if (it.execute("condition") as Boolean) it.execute("then") else it.execute("else") }
    functions["rock.control.loop"] = StandardFunction {
        while (true) {
            try {
                it.execute("body")
            } catch(b: LoopBreak) {
                if (b.interp == it) break
            } catch(c: LoopContinue) {
                if (c.interp == it) continue
            }
        }
    }
    functions["rock.control.loop.break"] = StandardFunction {
        throw LoopBreak(it.quickResolveKey("loop"))
    }
    functions["rock.control.loop.continue"] = StandardFunction {
        throw LoopBreak(it.quickResolveKey("loop"))
    }

    val blockVariables = HashMap<InterpretationInterface, HashMap<String, InterpretedPointer>>()
    functions["rock.control.block.variable.get"] = StandardFunction {
        val block = it.resolve(SubRef.Key("block"))
        val variable = it.resolve(SubRef.Key("variable")).key()
        blockVariables[block]!![variable]
    }
    functions["rock.control.block.variable"] = StandardFunction().apply {
        arguments["pointer"] = Reference.RCall(StandardCall("rock.control.block.variable.get").apply {
            arguments["block"] = Reference.RArgument(listOf(SubRef.Key("block")))
            arguments["variable"] = Reference.RArgument(listOf())
        })
    }
    functions["rock.control.block"] = StandardFunction {
        //allocate variables
        val myVars = HashMap<String, InterpretedPointer>()
        blockVariables[it] = myVars
        val variables = it.quickResolveKey("variables")
        for (key in variables.call().arguments.keys) {
            val vari = variables.quickResolveKey(key)
            myVars[key] = object : InterpretedPointer {
                override var value: Any? = vari.execute("value")
            }
        }

        //run the statements
        val result = it.executeSequence<Any?>("statements").last()

        //deallocate variables
        blockVariables.remove(it)

        result
    }.apply {
        arguments["variables"] = Reference.RCall(StandardCall(""))
    }
}