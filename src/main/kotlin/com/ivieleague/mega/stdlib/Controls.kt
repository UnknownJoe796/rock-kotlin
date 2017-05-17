package com.ivieleague.mega.stdlib

import com.ivieleague.mega.InterpretationInterface
import com.ivieleague.mega.StandardFunction
import com.ivieleague.mega.builder.execute
import com.ivieleague.mega.builder.executeSequence

class LoopBreak(val interp: InterpretationInterface) : Throwable()
class LoopContinue(val interp: InterpretationInterface) : Throwable()

fun StandardLibrary.controls() {
    functions["mega.control.if"] = StandardFunction { if (it.execute("condition") as Boolean) it.execute("then") else it.execute("else") }
    functions["mega.control.loop"] = StandardFunction {
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
    /*
    Variables Example
    block@block(
        variables = (
            x = variable( type = int  value = 3 )
        )
        statements = [
            set( this = getvar(declaration = @block.variables.x)  value = 3 )
        ]
    )
    */
    functions["mega.control.block.variable"] = StandardFunction {
        object : InterpretedPointer {
            override var value: Any? = it.execute("value")
        }
    }
    functions["mega.control.block"] = StandardFunction {

        it.executeSequence<Any?>("statements").last()
    }
}