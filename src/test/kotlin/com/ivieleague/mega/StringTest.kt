package com.ivieleague.mega

import com.ivieleague.mega.old.invokeAsRoot
import org.junit.Assert.assertEquals
import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class StringTest {
    @Test
    fun testLiteral() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret: "Test"
""")
        assertEquals("Test", program.invokeAsRoot())
    }

    @Test
    fun testConcat() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.string.concat
  values:
    - "This "
    - "is "
    - "a "
    - "test."
""")
        val result = program.invokeAsRoot()
        println(result)
        assertEquals("This is a test.", result)
    }

    @Test
    fun testJoin() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.string.join
  separator: " "
  values:
    - "This"
    - "is"
    - "a"
    - "test."
""")
        val result = program.invokeAsRoot()
        println(result)
        assertEquals("This is a test.", result)
    }
}