package com.ivieleague.rock.old

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.junit.Test
import java.io.File

/**
 * \n * Created by josep on 2/15/2017.
 */
class MainTest {

    val testProgramInterpret = """---
/interpret:
  =: =rock.main
  /: /interpret
  action:
    =: =rock.debug.print
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