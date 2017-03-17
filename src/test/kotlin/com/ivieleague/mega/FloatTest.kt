package com.ivieleague.mega

import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class FloatTest {

    @Test
    fun testLiteral() {
        val yml = """---
/: /interpret
/interpret: 4.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4.0)
    }

    @Test
    fun testSum() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.sum
  values:
    - 1.0
    - 2.0
    - 4.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 7.0)
    }

    @Test
    fun testProduct() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.product
  values:
    - 1.0
    - 2.0
    - 4.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 8.0)
    }

    @Test
    fun testSubtract() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.subtract
  left: 6.0
  right: 2.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4.0)
    }

    @Test
    fun testDivide() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.divide
  left: 6.0
  right: 2.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 3.0)
    }

    @Test
    fun testModulus() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.modulus
  left: 7.0
  right: 2.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 1.0)
    }

    @Test
    fun testNegative() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.negative
  value: 4.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == -4.0)
    }

    @Test
    fun testAbsolute() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.absolute
  value: -4.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4.0)
    }

    @Test
    fun testCompareTrue() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.compare
  larger: 4.0
  smaller: 3.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == true)
    }

    @Test
    fun testCompareFalse() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.float.8.compare
  larger: 4.0
  smaller: 5.0
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == false)
    }
}