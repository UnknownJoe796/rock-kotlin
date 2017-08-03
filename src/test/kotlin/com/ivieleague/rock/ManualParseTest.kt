package com.ivieleague.rock

import com.ivieleague.generic.PushbackReaderDebug
import org.junit.Test
import kotlin.test.assertEquals

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

    val referencesValid = listOf(
            "*lambda.child" to Reference.RLambdaArgument(listOf(SubRef.Key("lambda"), SubRef.Key("child"))),
            "@label.childA#0.childB" to Reference.RLabel("label", listOf(SubRef.Key("childA"), SubRef.Index(0), SubRef.Key("childB"))),
            ".childA#0.childB" to Reference.RArgument(listOf(SubRef.Key("childA"), SubRef.Index(0), SubRef.Key("childB"))),
            "/static.childA#0.childB" to Reference.RStatic("static", listOf(SubRef.Key("childA"), SubRef.Index(0), SubRef.Key("childB")))
    )

    @Test
    fun parseReferenceTest() {
        referencesValid.forEach {
            println(it.first)
            val reader = PushbackReaderDebug(it.first.reader(), 256)
            try {
                ManualRepresentation().run {
                    val result = reader.parseReference()
                    assert(result == it.second)
                }
            } catch(e: Throwable) {
                throw IllegalArgumentException("Broke at ${reader.position}", e)
            }
        }
    }

    val literalTwo = StandardCall(ManualRepresentation.LITERAL_INTEGER, literal = 2)
    val callsValid = listOf(
            "2" to literalTwo,
            "test(x=2)" to StandardCall("test").apply { arguments["x"] = Reference.RCall(literalTwo) },
            "test ( x = 2 )" to StandardCall("test").apply { arguments["x"] = Reference.RCall(literalTwo) },
            "[2]" to StandardCall(ManualRepresentation.LITERAL_LIST).apply { items.add(Reference.RCall((literalTwo))) },
            "\"test\"" to StandardCall(ManualRepresentation.LITERAL_STRING, literal = "test"),
            "\"test\"/language" to StandardCall(ManualRepresentation.LITERAL_STRING, literal = "test", language = "language"),
            "`test`/language" to StandardCall("rock.string.concatenateList", language = "language").apply {
                arguments["values"] = Reference.RCall(StandardCall("LIST").apply {
                    items += Reference.RCall(StandardCall(ManualRepresentation.LITERAL_STRING, literal = "test"))
                })
            },
            "`test\${.a}plus\${.b}template`/language" to StandardCall("rock.string.concatenateList", language = "language").apply {
                arguments["values"] = Reference.RCall(StandardCall("LIST").apply {
                    items += Reference.RCall(StandardCall(ManualRepresentation.LITERAL_STRING, literal = "test"))
                    items += Reference.RArgument(listOf(SubRef.Key("a")))
                    items += Reference.RCall(StandardCall(ManualRepresentation.LITERAL_STRING, literal = "plus"))
                    items += Reference.RArgument(listOf(SubRef.Key("b")))
                    items += Reference.RCall(StandardCall(ManualRepresentation.LITERAL_STRING, literal = "template"))
                })
            },
            "4f" to StandardCall(ManualRepresentation.LITERAL_FLOAT, literal = 4.0),
            "4.0" to StandardCall(ManualRepresentation.LITERAL_FLOAT, literal = 4.0)
    )

    @Test
    fun parseCallTest() {
        callsValid.forEach {
            println(it.first)
            val reader = PushbackReaderDebug(it.first.reader(), 256)
            val result = try {
                ManualRepresentation().run {
                    reader.parseCall()
                }
            } catch(e: Throwable) {
                throw IllegalArgumentException("Broke at ${reader.position}", e)
            }
            println("$result VS")
            println(it.second)
            assertEquals(it.second, result)
        }
    }

//    @Test
//    fun parseNumberTest() {
//        ManualRepresentation().run {
//            val reader = PushbackReaderDebug("""""".reader(), 256)
//            reader.parseNumber()
//        }
//    }
//
//    @Test
//    fun parseStringTest() {
//        ManualRepresentation().run {
//            val reader = PushbackReaderDebug("""""".reader(), 256)
//            reader.parseString()
//        }
//    }
//
//    @Test
//    fun parseCallFunctionTest() {
//        ManualRepresentation().run {
//            val reader = PushbackReaderDebug("""""".reader(), 256)
//            reader.parseCallFunction()
//        }
//    }
//
//    @Test
//    fun parseArgumentTest() {
//        ManualRepresentation().run {
//            val reader = PushbackReaderDebug("""""".reader(), 256)
//            reader.parseArgument()
//        }
//    }
}