package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class ArrayTest {

    val arrayInstructions = listOf(
            arrayOf(1, 2, 3) to """rock.array.literal(values=[1 2 3])""",
            arrayOf(1, 1, 1) to """rock.array.copies(value=1 size=3)""",
            arrayOf(1, 1, 1) to """rock.array.build(value=1 size=3)"""
    )
    val instructions = listOf(
            2 to """rock.pointer.get(this = rock.array.pointer(array = rock.array.literal(values=[1 2 3])  index = 1))"""
    )

    @Test fun runAllInstructions() {
        for ((expected, call) in arrayInstructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            val result = (StandardRoot().merge(root).merge(StandardLibrary).executeMain() as? Array<*>)!!
            assert(result.size == expected.size)
            for (index in result.indices) {
                assertEquals(expected[index], result[index])
            }
        }
        for ((expected, call) in instructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }
}