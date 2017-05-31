package com.ivieleague.rock

import com.ivieleague.rock.stdlib.StandardLibrary
import org.junit.Test

class PrintStandardLibrary {

    @Test fun printRoot() {
        println(ManualRepresentation().run { StandardLibrary.toMRString() })
    }
}