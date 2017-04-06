package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.mega.stdlib.StandardLibraryMeta
import org.junit.Test

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
}