package com.ivieleague.rock.old

import com.ivieleague.rock.old.stdlib.InterpretedPointer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * \n * Created by josep on 2/15/2017.
 */
class PointerTest {

    @Test
    fun testGet() {
        val standard = TestCommons.getStandardLibrary()
        val special = DSL {
            "examplePointer" - action {
                object : InterpretedPointer {
                    override var value: Any? = "Test"
                }
            }
        }
        val yaml = TestCommons.parse("""---
/: /interpret
/interpret:
  =: =rock.pointer.get
  this: =examplePointer
""")
        val result = MergedCall(arrayOf(yaml, special, standard)).invokeAsRoot()
        println(result)
        assertEquals("Test", result)
    }
}