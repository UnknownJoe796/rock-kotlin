package com.ivieleague.mega

import com.ivieleague.generic.Performance
import org.junit.Test
import java.io.File

/**

 * Created by josep on 2/15/2017.
 */
class OverheadTest {

    @Test
    fun printTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.debug.print
  /: /interpret
  value: "Hello World!"
""")
        val megaNanos = Performance.nanoseconds { program.invokeAsRoot() }
        val directNanos = Performance.nanoseconds { println("Hello World!") }
        println("Cost ratio: ${megaNanos / directNanos}")
    }

    @Test
    fun andTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.boolean.and
  /: /interpret
  values:
    - true
    - false
""")
        val t = true
        val f = false
        val megaNanos = Performance.nanoseconds { program.invokeAsRoot() }
        val directNanos = Performance.nanoseconds { sequenceOf(true, false).all { it } }
        println("Cost ratio: ${megaNanos / directNanos}")
    }

    @Test
    fun piTest() {
        val program = TestCommons.parsePlusStandardLibrary(File("./src/main/yaml/picalc.yaml").readText())
        val megaNanos = Performance.nanoseconds(10){
            program.invokeAsRoot()
        }
        val directNanos = Performance.nanoseconds(10) {
            var sum = 0.0
            var iter = 0.0
            while(iter < 10000.0){
                val top = if(iter % 2 == 0.0) 4.0 else -4.0
                val bottom = iter * 2 + 1
                sum += top / bottom
                iter += 1.0
            }
        }
        println("Cost ratio: ${megaNanos / directNanos}")
    }
}