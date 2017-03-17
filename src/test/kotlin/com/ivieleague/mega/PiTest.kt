package com.ivieleague.mega

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * \n * Created by josep on 2/15/2017.
 */
class PiTest {
    @Test
    fun testLiteral() {
        val program = TestCommons.parsePlusStandardLibrary(File("./src/main/yaml/picalc.yaml").readText())
        println(program.invokeAsRoot())
    }
}