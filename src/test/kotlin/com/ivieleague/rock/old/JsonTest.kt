package com.ivieleague.rock.old

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.rock.old.stdlib.StandardLibraryMeta
import org.junit.Test
import java.io.File

/**
 * Created by joseph on 4/5/17.
 */
class JsonTest {
    @Test
    fun printStandardLibrary() {
        val program = MergedCall(arrayOf(
                TestCommons.getStandardLibrary(),
                StandardLibraryMeta()
        ))
        println(ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(program)))
    }

    @Test
    fun printStandardLibraryMeta() {
        val program = StandardLibraryMeta()
        println(ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(program)))
    }

    @Test
    fun testContext() {
        val src = JSON.toCall(ObjectMapper(YAMLFactory()).readValue("""---
plusTwo:
  =: =rock.integer.signed.4.sum
  values:
    - =.value
    - 2
/interpret:
  =: =plusTwo
  /: /interpret
  value: 1
""", Map::class.java))
        val program = LazyMergedCall(listOf(
                src,
                TestCommons.getStandardLibrary()
        )).solidify()
        println(program.invokeAsRoot())
    }

    @Test
    fun lazyTest() {
        val merged = MergedCall(arrayOf(
                TestCommons.parse(File("./src/main/yaml/picalc.yaml").readText()),
                TestCommons.getStandardLibrary(),
                StandardLibraryMeta()
        ))
        val lazy = LazyMergedCall(listOf(
                TestCommons.parse(File("./src/main/yaml/picalc.yaml").readText()),
                TestCommons.getStandardLibrary(),
                StandardLibraryMeta()
        ))
        val strMerged = ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(merged))
        val strLazy = ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(lazy))
        println(strMerged)
        assert(strMerged == strLazy)
    }
}