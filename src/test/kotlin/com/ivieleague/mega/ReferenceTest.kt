package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.ivieleague.mega.builder.executeMain
import com.ivieleague.mega.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class ReferenceTest {

    val instructions = listOf(
            7 to """
            import mega.integer.signed.4.plus plus
            static = label@(
                x = 3
                child = (
                    label = @label.x
                )
            )
            main = plus(
                left = /static.child.label
                right = 4
            )
"""
    )

    @Test fun runAllInstructions() {
        for ((expected, text) in instructions) {
            val root = ManualRepresentation().run {
                val result = PushbackReader(StringReader(text), 256).parseFile()
                println(result.toMRString())
                result
            }
            println(ObjectMapper().writeValueAsString(PrimitiveConversion.run { root.toPrimitive() }))
            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }
}