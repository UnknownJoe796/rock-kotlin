package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.junit.Test
import java.io.File

/**
 * \n * Created by josep on 2/15/2017.
 */
class JavaTest {
    val testProgram = """---
/interpret:
  =: =mega.main
  /: /single_file_java
  action:
    =: =mega.debug.print
    value: "Hello World!"
"""

    @Test
    fun test() {
        val program = LazyMergedCall(listOf(
                JSON.toCall(ObjectMapper(YAMLFactory()).readValue(testProgram, Map::class.java)),
                JSON.toCall(ObjectMapper(YAMLFactory()).readValue(File("./src/main/yaml/single_file_java.yaml"), Map::class.java)),
                JSON.toCall(ObjectMapper(YAMLFactory()).readValue(File("./src/main/yaml/meta.yaml"), Map::class.java)),
                TestCommons.getStandardLibrary()
        )).solidify()
        println(ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(program)))
        println(program.invokeAsRoot())
    }
}