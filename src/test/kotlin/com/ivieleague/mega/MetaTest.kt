package com.ivieleague.mega

import com.ivieleague.mega.old.invokeAsRoot
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
  =: =mega.meta.exists
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
  =: =mega.meta.exists
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
  =: =mega.meta.forEach
  ref: =something
  statement:
    =: =mega.debug.print
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
  =: =mega.meta.forEach
  ref: =something
  statement:
    =: =mega.debug.print
    value:
      =: =mega.meta.forEach.index
      forEach: =@forEachLoop
""")
        program.invokeAsRoot()
    }
}