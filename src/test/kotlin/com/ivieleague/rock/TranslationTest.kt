package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class TranslationTest {

    val language = """
rock.integer.signed.4.plus - ( /language = rock.string.concatenateList/interpret(values=[
    "Add "
    .left
    " and "
    .right
    " together"
]))

rock.integer.signed.4.literal - ( /language = rock.integer.signed.4_to_mega.string/interpret( value = rock.reflection.literal( value = . ) ) )
"""

    val languageRoot = ManualRepresentation().run {
        PushbackReader(StringReader(language)).parseFile()
    }

    val instructions = listOf(
            "Add 3 and 4 together" to """main = rock.integer.signed.4.plus/language( left = 3  right = 4 )"""
    )

    @Test fun runAllInstructions() {
        for ((expected, text) in instructions) {
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            println(ManualRepresentation().run { StandardRoot().merge(root).merge(languageRoot).toMRString() })
            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).merge(languageRoot).executeMain())
        }
    }
}