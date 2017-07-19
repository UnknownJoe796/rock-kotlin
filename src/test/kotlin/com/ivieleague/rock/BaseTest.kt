package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class BaseTest {

    val instructions = listOf(
            Unit to """execute( base=rock.debug.print(value=*value) arguments=(value="Output") )""",
            6 to """execute( base=rock.integer.signed.4.plus(left=*left right=*right) arguments=(left = 4  right = 2) )"""
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
}