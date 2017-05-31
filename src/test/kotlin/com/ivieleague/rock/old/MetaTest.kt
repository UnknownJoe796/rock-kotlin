package com.ivieleague.rock.old

import org.junit.Assert
import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class MetaTest {
    @Test
    fun testExistsTrue() {
        val program = TestCommons.parsePlusStandardLibrary("""---
something: true
/: /interpret
/interpret:
  =: =rock.meta.exists
  ref: =something
""")
        Assert.assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun testExistsFalse() {
        val program = TestCommons.parsePlusStandardLibrary("""---
something: true
/: /interpret
/interpret:
  =: =rock.meta.exists
  ref: =somethingElse
""")
        Assert.assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun testForEachItem() {
        val program = TestCommons.parsePlusStandardLibrary("""---
something:
  - "Item 1"
  - "Item 2"
  - "Item 3"
/: /interpret
/interpret:
  "@": forEachLoop
  =: =rock.meta.forEach
  ref: =something
  statement:
    =: =rock.debug.print
    value: =@forEachLoop.item
""")
        program.invokeAsRoot()
    }

    @Test
    fun testForEachIndex() {
        val program = TestCommons.parsePlusStandardLibrary("""---
something:
  - "Item 1"
  - "Item 2"
  - "Item 3"
/: /interpret
/interpret:
  "@": forEachLoop
  =: =rock.meta.forEach
  ref: =something
  statement:
    =: =rock.debug.print
    value:
      =: =rock.meta.forEach.index
      forEach: =@forEachLoop
""")
        program.invokeAsRoot()
    }
}