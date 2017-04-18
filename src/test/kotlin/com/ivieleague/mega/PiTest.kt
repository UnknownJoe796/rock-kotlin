package com.ivieleague.mega

import com.ivieleague.mega.old.invokeAsRoot
import com.ivieleague.mega.old.same
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

    @Test
    fun testLiteralLazy() {
        val program = TestCommons.parsePlusStandardLibraryAlternate(File("./src/main/yaml/picalc.yaml").readText())
        println(program.invokeAsRoot())
    }

    @Test
    fun testSame() {
        val first = TestCommons.parsePlusStandardLibrary(File("./src/main/yaml/picalc.yaml").readText())
        val second = TestCommons.parsePlusStandardLibraryAlternate(File("./src/main/yaml/picalc.yaml").readText())
        println(first same second)
    }
}