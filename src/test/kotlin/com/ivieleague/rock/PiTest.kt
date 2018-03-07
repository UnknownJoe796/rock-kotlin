package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.File
import java.io.PushbackReader
import java.io.StringReader

class PiTest {
    @Test
    fun testPi() {
        val text = File("./src/main/manrep/picalc.rock").readText()
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        println(StandardRoot().merge(root).merge(StandardLibrary).executeMain())
    }

    @Test
    fun testPiKotlin() {
        val kotlin = ManualRepresentation().run {
            PushbackReader(StringReader(File("./src/main/manrep/kotlin.rock").readText()), 256).parseFile()
        }
        val text = File("./src/main/manrep/picalc.rock").readText()
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        root.calls["main"].apply { (this as StandardCall).language = "kotlin" }
        println(StandardRoot().merge(root).merge(kotlin).merge(StandardLibrary).executeMain())
    }

    @Test
    fun testPiShortA() {
        val text = File("./src/main/manrep/picalcShortA.rock").readText()
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        val program = StandardRoot().merge(root).merge(StandardLibrary)
        println(program.executeMain())
    }
}