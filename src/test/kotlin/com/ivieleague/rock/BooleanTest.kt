package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class BooleanTest {

    val instructions = listOf(
            true to """rock.boolean.and(left = rock.boolean.true()  right = rock.boolean.true())""",
            false to """rock.boolean.and(left = rock.boolean.true()  right = rock.boolean.false())""",
            false to """rock.boolean.and(left = rock.boolean.false()  right = rock.boolean.true())""",
            false to """rock.boolean.and(left = rock.boolean.false()  right = rock.boolean.false())""",

            true to """rock.boolean.or(left = rock.boolean.true()  right = rock.boolean.true())""",
            true to """rock.boolean.or(left = rock.boolean.true()  right = rock.boolean.false())""",
            true to """rock.boolean.or(left = rock.boolean.false()  right = rock.boolean.true())""",
            false to """rock.boolean.or(left = rock.boolean.false()  right = rock.boolean.false())""",

            false to """rock.boolean.xor(left = rock.boolean.true()  right = rock.boolean.true())""",
            true to """rock.boolean.xor(left = rock.boolean.true()  right = rock.boolean.false())""",
            true to """rock.boolean.xor(left = rock.boolean.false()  right = rock.boolean.true())""",
            false to """rock.boolean.xor(left = rock.boolean.false()  right = rock.boolean.false())""",

            true to """rock.boolean.equals(left = rock.boolean.true()  right = rock.boolean.true())""",
            false to """rock.boolean.equals(left = rock.boolean.true()  right = rock.boolean.false())""",
            false to """rock.boolean.equals(left = rock.boolean.false()  right = rock.boolean.true())""",
            true to """rock.boolean.equals(left = rock.boolean.false()  right = rock.boolean.false())""",

            false to """rock.boolean.not(value = rock.boolean.true())""",
            true to """rock.boolean.not(value = rock.boolean.false())""",

            true to """rock.boolean.all(values = [])""",
            true to """rock.boolean.all(values = [rock.boolean.true() rock.boolean.true()])""",
            false to """rock.boolean.all(values = [rock.boolean.true() rock.boolean.false()])""",
            false to """rock.boolean.all(values = [rock.boolean.false() rock.boolean.true()])""",
            false to """rock.boolean.all(values = [rock.boolean.false() rock.boolean.false()])""",

            false to """rock.boolean.any(values = [])""",
            true to """rock.boolean.any(values = [rock.boolean.true() rock.boolean.true()])""",
            true to """rock.boolean.any(values = [rock.boolean.true() rock.boolean.false()])""",
            true to """rock.boolean.any(values = [rock.boolean.false() rock.boolean.true()])""",
            false to """rock.boolean.any(values = [rock.boolean.false() rock.boolean.false()])""",

            false to """rock.boolean.xorList(values = [])""",
            false to """rock.boolean.xorList(values = [rock.boolean.true() rock.boolean.true()])""",
            true to """rock.boolean.xorList(values = [rock.boolean.true() rock.boolean.false()])""",
            true to """rock.boolean.xorList(values = [rock.boolean.false() rock.boolean.true()])""",
            false to """rock.boolean.xorList(values = [rock.boolean.false() rock.boolean.false()])"""
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