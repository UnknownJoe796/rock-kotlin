package com.ivieleague.rock

import com.ivieleague.generic.Performance
import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.PushbackReader
import java.io.StringReader
import kotlin.test.assertEquals

class NumbersTest {

    val instructions = listOf(
            """rock.integer.signed.4.plus(left = 1 right = 2)""" to 3,
            """rock.integer.signed.4.sum(values = [1 2 3 4])""" to 10,
            """rock.integer.signed.4.times(left = 4 right = 2)""" to 8,
            """rock.float.8.power(value = 2.0 exponent = 3.0)""" to 8.0,
            """rock.float.8.absolute(value = -3.0)""" to 3.0,
            """rock.float.8.ceiling(value = 2.25)""" to 3.0,
            """rock.float.8.floor(value = 2.75)""" to 2.0,
            """rock.float.8.squareRoot(value = 4.0)""" to 2.0,
            """rock.float.8.round(value = 3.6)""" to 4.0,
            """rock.float.8.sin(value = 0.0)""" to 0.0,
            """rock.float.8.cos(value = 0.0)""" to 1.0,
            """rock.float.8.tan(value = 0.0)""" to 0.0,
            """rock.float.8.asin(value = 0.0)""" to 0.0,
            """rock.float.8.acos(value = 1.0)""" to 0.0,
            """rock.float.8.atan(value = 0.0)""" to 0.0,
            """rock.float.8.log(value = 2.0)""" to Math.log(2.0),
            """rock.float.8.log10(value = 12.0)""" to Math.log10(12.0),
            """rock.float.8.negate(value = 2.0)""" to -2.0,
            """rock.float.8.absolute(value = -2.0)""" to 2.0,
            """rock.float.8.plus(left = 3.0 right = 4.0)""" to 7.0,
            """rock.float.8.minus(left = 3.0 right = 4.0)""" to -1.0,
            """rock.float.8.times(left = 3.0 right = 4.0)""" to 12.0,
            """rock.float.8.divide(left = 3.0 right = 4.0)""" to .75,
            """rock.float.8.remainder(left = 3.0 right = 4.0)""" to 3.0,
            """rock.float.8.equal(left = 3.0 right = 4.0)""" to false,
            """rock.float.8.compare(lesser = 3.0 greater = 4.0)""" to true,
            """rock.float.8.sum(values = [1.5 2.5 3.5])""" to 7.5,
            """rock.float.8.product(values = [1.0 2.0 3.0 0.5])""" to 3.0
    )

    @Test fun runAllInstructions() {
        for ((call, expected) in instructions) {
            println("Testing $call")
            val text = "main = $call"
            val root = ManualRepresentation().run {
                PushbackReader(StringReader(text), 256).parseFile()
            }

            assertEquals(expected, StandardRoot().merge(root).merge(StandardLibrary).executeMain())
        }
    }

    @Test fun plusPerformanceRatio() {
        val text = "main = rock.integer.signed.4.plus(left = 1 right = 2)"
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { 4 + 2 })
    }

    @Test fun sumPerformanceRatio() {
        val text = "main = rock.integer.signed.4.sum(values = [1 2 3 4 5])"
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val fullRoot = StandardRoot().merge(root).merge(StandardLibrary)
        val list = listOf(1, 2, 3, 4, 5)
        println(Performance.seconds(1000000) { fullRoot.executeMain() } / Performance.seconds(1000000) { list.fold(0, { a, b -> a + b }) })
    }
}