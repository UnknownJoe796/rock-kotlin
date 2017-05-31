package com.ivieleague.rock

import com.ivieleague.generic.Performance
import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class ControlTest {

    val instructions = listOf(
            "Test" to """main = rock.control.if(condition = rock.boolean.true()  then = "Test"  else = "Nope")""",
            "Nope" to """main = rock.control.if(condition = rock.boolean.false()  then = "Test"  else = "Nope")""",
            "Block" to """main = rock.control.block(variables = ()  statements = ["Block"])""",
            "GetVar" to """main = block@rock.control.block(
                variables = (
                    test = rock.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    rock.pointer.get(this = rock.control.block.variable.get( block=@block variable=@block.variables.test ))
                ]
            )
""",
            "GetVar" to """main = block@rock.control.block(
                variables = (
                    test = rock.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    rock.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            "NewValue" to """main = block@rock.control.block(
                variables = (
                    test = rock.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    rock.pointer.set(this = @block.variables.test.pointer  value = "NewValue")
                    rock.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            "GetVar" to """main = block@rock.control.block(
                variables = (
                    ignore = rock.control.block.variable( block = @block  value = "GetVar" )
                    test = rock.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    rock.pointer.set(this = @block.variables.ignore.pointer  value = "NewValue")
                    rock.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            45 to """
            import rock.control.if if
            import rock.control.block block
            import rock.control.block.variable blockVar
            import rock.control.loop loop
            import rock.control.loop.break break
            import rock.integer.signed.4.plus plus
            import rock.integer.signed.4.compare compare
            import rock.pointer.set set
            import rock.pointer.get get

            main = block@block(
                variables = (
                    sum = blockVar( block = @block  value = 0 )
                    i = blockVar( block = @block  value = 0 )
                )
                statements = [
                    loop@loop(body = block(statements=[
                        if(
                            condition = compare( lesser = get(this = @block.variables.i.pointer) greater = 10 )
                            then = ()
                            else = break(loop = @loop)
                        )
                        rock.debug.print( value = get(this = @block.variables.i.pointer))
                        set(this = @block.variables.sum.pointer  value = plus(
                            left = get(this = @block.variables.sum.pointer)
                            right = get(this = @block.variables.i.pointer)
                        ))
                        set(this = @block.variables.i.pointer  value = plus(
                            left = get(this = @block.variables.i.pointer)
                            right = 1
                        ))
                    ]))
                    get(this = @block.variables.sum.pointer)
                ]
            )
"""
    )

    @Test fun runAllInstructions() {
        for ((expected, text) in instructions) {
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            println(ManualRepresentation().run { root.toMRString() })
            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }

    @Test fun ifPerformanceRatio() {
        val text = """main = rock.control.if(condition = rock.boolean.true()  then = "Test"  else = "Nope")"""
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        var test = true
        println(Performance.seconds(10000) { fullRoot.executeMain() } / Performance.seconds(10000) {
            test = !test
            val out = if (test) "Test" else "Nope"
        })
    }
}