package com.ivieleague.mega

import com.ivieleague.generic.Performance
import com.ivieleague.mega.builder.executeMain
import com.ivieleague.mega.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class ControlTest {

    val instructions = listOf(
            "Test" to """main = mega.control.if(condition = mega.boolean.true()  then = "Test"  else = "Nope")""",
            "Nope" to """main = mega.control.if(condition = mega.boolean.false()  then = "Test"  else = "Nope")""",
            "Block" to """main = mega.control.block(variables = ()  statements = ["Block"])""",
            "GetVar" to """main = block@mega.control.block(
                variables = (
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.get(this = mega.control.block.variable.get( block=@block variable=@block.variables.test ))
                ]
            )
""",
            "GetVar" to """main = block@mega.control.block(
                variables = (
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            "NewValue" to """main = block@mega.control.block(
                variables = (
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.set(this = @block.variables.test.pointer  value = "NewValue")
                    mega.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            "GetVar" to """main = block@mega.control.block(
                variables = (
                    ignore = mega.control.block.variable( block = @block  value = "GetVar" )
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.set(this = @block.variables.ignore.pointer  value = "NewValue")
                    mega.pointer.get(this = @block.variables.test.pointer)
                ]
            )
""",
            45 to """
            import mega.control.if if
            import mega.control.block block
            import mega.control.block.variable blockVar
            import mega.control.loop loop
            import mega.control.loop.break break
            import mega.integer.signed.4.plus plus
            import mega.integer.signed.4.compare compare
            import mega.pointer.set set
            import mega.pointer.get get

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
                        mega.debug.print( value = get(this = @block.variables.i.pointer))
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
        val text = """main = mega.control.if(condition = mega.boolean.true()  then = "Test"  else = "Nope")"""
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