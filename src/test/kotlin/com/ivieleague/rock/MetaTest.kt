package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class MetaTest {

    val instructions = listOf(
            Unit to """main = rock.meta.comment( value = "COMMENT TIME!" )"""
    )

    @Test
    fun runAllInstructions() {
        for ((expected, call) in instructions) {
            println("Testing $call")
            val text = "$call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }
}