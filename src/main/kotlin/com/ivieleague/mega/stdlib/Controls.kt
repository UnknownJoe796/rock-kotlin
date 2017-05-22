package com.ivieleague.mega.stdlib

import com.ivieleague.mega.*
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
    import mega.control.block as block
    import mega.control.block.variable as variable
    import mega.control.block.variable.pointer as getBlockVarPointer
    import mega.integer.signed.4.type as int
    import mega.pointer.set as set
    variable - (
        pointer = getBlockVarPointer( declaration = . )
    )
    main = block@block(
        variables = (
            x = variable( type = int  value = 3 )
        )
        statements = [
            set( this = getBlockVarPointer(declaration = @block.variables.x)  value = 3 )
            //Alt, using default from the variable declaration function?
            set( this = @block.variables.x.pointer  value = 3 )
        ]
    )
    */
    val blockVariables = HashMap<Pair<InterpretationInterface, Call>, InterpretedPointer>()
    functions["mega.control.block.variable.pointer"] = StandardFunction {
        val block = it.resolve(SubRef.Key("block"))
        val variable = it.resolve(SubRef.Key("variable")).call()
        blockVariables[block to variable]!!
    }
    functions["mega.control.block.variable"] = StandardFunction {
        object : InterpretedPointer {
            override var value: Any? = it.execute("value")
        }
    }.apply {
        arguments["pointer"] = Reference.RCall(StandardCall("mega.control.block.variable.pointer").also {
            arguments["block"] = Reference.RArgument(listOf())
            arguments["variable"] = Reference.RArgument(listOf())
        })
    }
    functions["mega.control.block"] = StandardFunction {

        it.executeSequence<Any?>("statements").last()
    }
}