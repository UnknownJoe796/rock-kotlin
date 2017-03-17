package com.ivieleague.mega

import org.junit.Test

/**

 * Created by josep on 2/15/2017.
 */
class IntegerTest {

    @Test
    fun testLiteral() {
        val yml = """---
/: /interpret
/interpret: 4
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4)
    }

    @Test
    fun testSum() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.sum
  values:
    - 1
    - 2
    - 4
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 7)
    }

    @Test
    fun testProduct() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.product
  values:
    - 1
    - 2
    - 4
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 8)
    }

    @Test
    fun testSubtract() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.subtract
  left: 6
  right: 2
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4)
    }

    @Test
    fun testDivide() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.divide
  left: 6
  right: 2
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 3)
    }

    @Test
    fun testModulus() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.modulus
  left: 7
  right: 2
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 1)
    }

    @Test
    fun testNegative() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.negative
  value: 4
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == -4)
    }

    @Test
    fun testAbsolute() {
        val yml = """---
/: /interpret
/interpret:
  =: =mega.integer.signed.4.absolute
  value: -4
"""
        val program = TestCommons.parsePlusStandardLibrary(yml)

        val result = program.invokeAsRoot()
        assert(result == 4)
    }
}