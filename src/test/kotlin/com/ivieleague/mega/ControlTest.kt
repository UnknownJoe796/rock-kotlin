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
            "Test" to """mega.control.if(condition = mega.boolean.true()  then = "Test"  else = "Nope")""",
            "Nope" to """mega.control.if(condition = mega.boolean.false()  then = "Test"  else = "Nope")""",
            "Block" to """mega.control.block(variables = ()  statements = ["Block"])""",
            "GetVar" to """block@mega.control.block(
                variables = (
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.get(this = mega.control.block.variable.get( block=@block variable=@block.variables.test ))
                ]
            )
""",
            "GetVar" to """block@mega.control.block(
                variables = (
                    test = mega.control.block.variable( block = @block  value = "GetVar" )
                )
                statements = [
                    mega.pointer.get(this = @block.variables.test.pointer)
                ]
            )
"""
    )

    @Test fun runAllInstructions() {
        for ((expected, call) in instructions) {
            val text = "main = $call"
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