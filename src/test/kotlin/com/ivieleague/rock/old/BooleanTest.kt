package com.ivieleague.rock.old

import org.junit.Assert.assertEquals
import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class BooleanTest {
    @Test
    fun trueTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret: true
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun falseTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret: false
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun notOnFalseTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.not
  value: false
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun notOnTrueTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.not
  value: true
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun andTrueTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.and
  values:
    - true
    - true
    - true
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun andFalse1Test() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.and
  values:
    - false
    - true
    - true
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun andFalse2Test() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.and
  values:
    - true
    - false
    - true
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun andFalseAllTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.and
  values:
    - false
    - false
    - false
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun orTrueAllTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.or
  values:
    - true
    - true
    - true
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun orTrue1Test() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.or
  values:
    - false
    - false
    - true
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun orFalseTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.or
  values:
    - false
    - false
    - false
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun xorTrueTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.xor
  values:
    - false
    - true
    - false
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun xorFalseTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.xor
  values:
    - false
    - true
    - true
""")
        assertEquals(false, program.invokeAsRoot())
    }

    @Test
    fun equalTrueTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.equal
  values:
    - false
    - false
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun equalTrueAltTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.equal
  values:
    - true
    - true
""")
        assertEquals(true, program.invokeAsRoot())
    }

    @Test
    fun equalFalseTest() {
        val program = TestCommons.parsePlusStandardLibrary("""---
/: /interpret
/interpret:
  =: =rock.boolean.equal
  values:
    - false
    - true
""")
        assertEquals(false, program.invokeAsRoot())
    }
}