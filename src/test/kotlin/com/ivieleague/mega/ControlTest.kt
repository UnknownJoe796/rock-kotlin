package com.ivieleague.mega

import org.junit.Assert.assertEquals
import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class ControlTest {
    @Test
    fun testIfConsequent() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.if
  condition: true
  consequent: "Success"
  alternative: "Failure"
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testIfAlternative() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.if
  condition: false
  consequent: "Failure"
  alternative: "Success"
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testBlock() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.block
  variables:
  statements:
    - "Ignore me!"
    - =: =mega.debug.print
      value: "Hi!"
    - "Success"
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testBlockVariable() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.block
  "@": block
  variables:
    var:
      =: =mega.control.block.variable
      value: "Success"
      block: =@block
  statements:
    - =: =mega.debug.print
      value: "Starting..."

    - =: =mega.pointer.get
      this: =@block.variables.var
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testBlockModifyVariable() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.block
  "@": block
  variables:
    var:
      =: =mega.control.block.variable
      value: "Failure"
      block: =@block
  statements:
    - =: =mega.debug.print
      value: "Starting..."
    - =: =mega.type.set
      this: =@block.variables.var
      value: "Success"
    - =: =mega.pointer.get
      this: =@block.variables.var
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testLoopBreak() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.loop
  "@": loop
  body:
    =: =mega.control.loop.break
    loop: =@loop
    value: "Success"
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testLoopCounter() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =mega.control.block
  "@": block
  variables:
    i:
      =: =mega.control.block.variable
      value: 0
      block: =@block
  statements:
    - =: =mega.control.loop
      "@": loop
      body:
        =: =mega.control.block
        variables:
        statements:
          - =: =mega.type.set
            this: =@block.variables.i
            value:
              =: =mega.integer.signed.4.sum
              values:
                - =: =mega.pointer.get
                  this: =@block.variables.i
                - 1
          - =: =mega.debug.print
            value:
              =: =mega.pointer.get
              this: =@block.variables.i
          - =: =mega.control.if
            condition:
              =: =mega.integer.signed.4.compare
              smaller:
                =: =mega.pointer.get
                this: =@block.variables.i
              larger: 10
            consequent: null
            alternative:
              =: =mega.control.loop.break
              loop: =@loop
              value: "Success"


""")
        assertEquals("Success", program.invokeAsRoot())
    }
}