package com.ivieleague.mega

import com.ivieleague.generic.Performance
import com.ivieleague.mega.builder.executeMain
import com.ivieleague.mega.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class NumbersTest {

    val instructions = listOf(
            """mega.integer.signed.4.plus(left = 1 right = 2)""" to 3,
            """mega.integer.signed.4.sum(values = [1 2 3 4])""" to 10,
            """mega.integer.signed.4.times(left = 4 right = 2)""" to 8
    )

    @Test fun runAllInstructions() {
        for ((call, expected) in instructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }

    @Test fun plusPerformanceRatio() {
        val text = "main = mega.integer.signed.4.plus(left = 1 right = 2)"
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { 4 + 2 })
    }

    @Test fun sumPerformanceRatio() {
        val text = "main = mega.integer.signed.4.sum(values = [1 2 3 4 5])"
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        val list = listOf(1, 2, 3, 4, 5)
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { list.fold(0, { a, b -> a + b }) })
    }
}