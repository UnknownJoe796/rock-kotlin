package com.ivieleague.rock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class DemonstrationTest {

    val instructions = listOf(
            3 to """rock.integer.signed.4.plus(left = 1 right = 2)""",
            8.0 to """rock.float.8.power(value = 2.0 exponent = 3.0)""",
            "This is a test." to """rock.string.concatenate(left = "This is a " right = "test.")""",
            "This is a test." to """rock.string.concatenateList(values = ["This " "is " "a " "test."])""",
            "This is a test." to """rock.string.join(values = ["This" "is" "a" "test."] separator = " ")""",
            "This is not a test." to """rock.string.join(values = ["This" "is" rock.control.if(
                condition = rock.boolean.true()
                then = "not"
                else = "really"
            ) "a" "test."] separator = " ")"""
    )

    @Test fun runAllInstructions() {
        for ((expected, call) in instructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            println(ManualRepresentation().run { root.toMRString() })
            println(ObjectMapper().writeValueAsString(PrimitiveConversion.run { root.toPrimitive() }))
            println(ObjectMapper(YAMLFactory()).writeValueAsString(PrimitiveConversion.run { root.toPrimitive() }))

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }
}