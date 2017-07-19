package com.ivieleague.rock

import com.ivieleague.rock.builder.executeMain
import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test
import java.io.File
import java.io.PushbackReader
import java.io.StringReader

class PiTest {
    @Test fun testPi() {
        val text = File("./src/main/manrep/picalc.rock").readText()
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        println(StandardRoot().merge(root).merge(StandardLibrary).executeMain())
    }

    @Test fun testPiShortA() {
        val text = File("./src/main/manrep/picalcShortA.rock").readText()
        val root = ManualRepresentation().run {
            PushbackReader(StringReader(text), 256).parseFile()
        }
        println(StandardRoot().merge(root).merge(StandardLibrary).executeMain())
    }
}