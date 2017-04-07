package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.mega.stdlib.StandardLibraryMeta
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
        assert(strMerged == strLazy)
    }
}