package com.ivieleague.rock.old

import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class DebugTest {
    @Test
    fun printTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.debug.print
  /: /interpret
  value: "Hello World!"
""")
        program.invokeAsRoot()
    }
    @Test
    fun infoTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.debug.info
  /: /interpret
  value: =otherData.something
otherData:
  =: =protoData
protoData:
  something: "hello"
""")
//        println(TestCommons.toYaml(program))
        println(program.invokeAsRoot())
    }
    @Test
    fun infoDirectTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.debug.info
  /: /interpret
  value: "hello"
""")
//        println(TestCommons.toYaml(program))
        println(program.invokeAsRoot())
    }
}