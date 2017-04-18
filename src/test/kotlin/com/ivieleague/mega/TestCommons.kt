package com.ivieleague.mega

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.mega.old.JSON
import com.ivieleague.mega.old.LazyMergedCall
import com.ivieleague.mega.old.MergedCall
import com.ivieleague.mega.old.invokeAsRoot
import com.ivieleague.mega.old.stdlib.StandardLibrary

/**
 *
 * \n * Created by josep on 2/25/2017.
 */
object TestCommons {
    fun getStandardLibrary(): Call = StandardLibrary()
    fun parse(yaml: String) = JSON.toCall(ObjectMapper(YAMLFactory()).readValue(yaml, Map::class.java))
    fun parsePlusStandardLibrary(yaml: String) = MergedCall(arrayOf(parse(yaml), getStandardLibrary()))
    fun parsePlusStandardLibraryAlternate(yaml: String) = LazyMergedCall(listOf(parse(yaml), getStandardLibrary()))
    fun execute(yaml: String) = parsePlusStandardLibrary(yaml).invokeAsRoot()
    fun toYaml(program: Call): String {
        return ObjectMapper(YAMLFactory()).writeValueAsString(JSON.fromCall(program))
    }
}