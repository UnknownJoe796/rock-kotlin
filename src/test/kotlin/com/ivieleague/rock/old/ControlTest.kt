package com.ivieleague.rock.old

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
  =: =rock.control.if
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
  =: =rock.control.if
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
  =: =rock.control.block
  variables:
  statements:
    - "Ignore me!"
    - =: =rock.debug.print
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
  =: =rock.control.block
  "@": block
  variables:
    var:
      =: =rock.control.block.variable
      value: "Success"
      block: =@block
  statements:
    - =: =rock.debug.print
      value: "Starting..."

    - =: =rock.pointer.get
      this: =@block.variables.var
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testBlockModifyVariable() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.control.block
  "@": block
  variables:
    var:
      =: =rock.control.block.variable
      value: "Failure"
      block: =@block
  statements:
    - =: =rock.debug.print
      value: "Starting..."
    - =: =rock.type.set
      this: =@block.variables.var
      value: "Success"
    - =: =rock.pointer.get
      this: =@block.variables.var
""")
        assertEquals("Success", program.invokeAsRoot())
    }

    @Test
    fun testLoopBreak() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.control.loop
  "@": loop
  body:
    =: =rock.control.loop.break
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
  =: =rock.control.block
  "@": block
  variables:
    i:
      =: =rock.control.block.variable
      value: 0
      block: =@block
  statements:
    - =: =rock.control.loop
      "@": loop
      body:
        =: =rock.control.block
        variables:
        statements:
          - =: =rock.type.set
            this: =@block.variables.i
            value:
              =: =rock.integer.signed.4.sum
              values:
                - =: =rock.pointer.get
                  this: =@block.variables.i
                - 1
          - =: =rock.debug.print
            value:
              =: =rock.pointer.get
              this: =@block.variables.i
          - =: =rock.control.if
            condition:
              =: =rock.integer.signed.4.compare
              smaller:
                =: =rock.pointer.get
                this: =@block.variables.i
              larger: 10
            consequent: null
            alternative:
              =: =rock.control.loop.break
              loop: =@loop
              value: "Success"


""")
        assertEquals("Success", program.invokeAsRoot())
    }
}