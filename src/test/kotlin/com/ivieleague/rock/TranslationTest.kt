package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.File
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

rock.integer.signed.4.literal - ( /language = rock.integer.signed.4_to_rock.string/interpret( value = rock.reflection.literal( value = . ) ) )

base - ( /language = "UNKNOWN"/interpret )

"""

    val languageRoot = ManualRepresentation().run {
        PushbackReader(StringReader(language)).parseFile()
    }

    val instructions = listOf(
            "Add 3 and 4 together" to """main = rock.integer.signed.4.plus/language( left = 3  right = 4 )""",
            "UNKNOWN" to """main = rock.void.literal/language()""",
            "(3 + 4).toInt()" to """main = rock.integer.signed.4.plus/kotlin( left = 3  right = 4 )"""
    )

    @Test fun runAllInstructions() {
        val kotlin = ManualRepresentation().run {
            PushbackReader(StringReader(File("./src/main/manrep/kotlin.rock").readText()), 256).parseFile()
        }
        for ((expected, text) in instructions) {
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            val combined = StandardRoot().merge(StandardLibrary).merge(languageRoot).merge(kotlin).merge(root)
//            println(ManualRepresentation().run { combined.toMRString() })
            val result = combined.executeMain()
            println(result)
            assertEquals(expected, result)
        }
    }
}