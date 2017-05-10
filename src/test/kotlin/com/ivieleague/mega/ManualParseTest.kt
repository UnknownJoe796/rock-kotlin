package com.ivieleague.mega

import com.ivieleague.generic.PushbackReaderDebug
import org.junit.Test

/**
 * Created by josep on 5/10/2017.
 */
class ManualParseTest {
    @Test
    fun parseFileTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseFile()
        }
    }

    val functionsValid = listOf(
            "()" to StandardFunction(),
            """(test = 3)""" to StandardFunction().apply { arguments["test"] = Reference.RCall(StandardCall(ManualRepresentation.LITERAL_INTEGER, literal = 3)) }
    )

    @Test
    fun parseFunctionTest() {
        functionsValid.forEach {
            println(it.first)
            val reader = PushbackReaderDebug(it.first.reader(), 256)
            try {
                ManualRepresentation().run {
                    assert(reader.parseFunction() == it.second)
                }
            } catch(e: Throwable) {
                throw IllegalArgumentException("Broke at ${reader.position}", e)
            }
        }
    }

    @Test
    fun parseReferenceTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseReference()
        }
    }

    @Test
    fun parseCallTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseCall()
        }
    }

    @Test
    fun parseNumberTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseNumber()
        }
    }

    @Test
    fun parseStringTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseString()
        }
    }

    @Test
    fun parseCallFunctionTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseCallFunction()
        }
    }

    @Test
    fun parseArgumentTest() {
        ManualRepresentation().run {
            val reader = PushbackReaderDebug("""""".reader(), 256)
            reader.parseArgument()
        }
    }
}