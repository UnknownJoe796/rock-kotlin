package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.mega.old.LazyMergedCall
import com.ivieleague.mega.old.invokeAsRoot
import com.ivieleague.mega.old.solidify
import org.junit.Test
import java.io.File

/**
 * \n * Created by josep on 2/15/2017.
 */
class MainTest {

    val testProgramInterpret = """---
/interpret:
  =: =mega.main
  /: /interpret
  action:
    =: =mega.debug.print
    value: "Hello World!"
"""

    @Test
    fun testInterpret() {
        val program = LazyMergedCall(listOf(
                JSON.toCall(ObjectMapper(YAMLFactory()).readValue(testProgramInterpret, Map::class.java)),
                JSON.toCall(ObjectMapper(YAMLFactory()).readValue(File("./src/main/yaml/meta.yaml"), Map::class.java)),
                TestCommons.getStandardLibrary()
        )).solidify()
        println(program.invokeAsRoot())
    }
}