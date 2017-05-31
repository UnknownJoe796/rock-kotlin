package com.ivieleague.rock

import com.ivieleague.generic.Performance
import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class StringTest {

    val instructions = listOf(
            "This is a test." to """rock.string.concatenate(left = "This is a " right = "test.")""",
            "This is a test." to """rock.string.concatenateList(values = ["This " "is " "a " "test."])""",
            "This is a test." to """rock.string.join(values = ["This" "is" "a" "test."] separator = " ")"""
    )

    @Test fun runAllInstructions() {
        for ((expected, call) in instructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }

    @Test fun concatPerformanceRatio() {
        val text = """main = rock.string.concatenate(left = "This is a " right = "test.")"""
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        val start = "This is a"
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { start + "test." })
    }

    @Test fun concatListPerformanceRatio() {
        val text = """main = rock.string.join(values = ["This" "is" "a" "test."] separator = " ")"""
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        val list = listOf("This", "is", "a", "test.")
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { list.joinToString("") })
    }
}