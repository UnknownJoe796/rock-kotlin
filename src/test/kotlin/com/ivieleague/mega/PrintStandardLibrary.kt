package com.ivieleague.mega

import com.ivieleague.mega.stdlib.StandardLibrary
import org.junit.Test

class PrintStandardLibrary {

    @Test fun printRoot() {
        println(ManualRepresentation().run { StandardLibrary.toMRString() })
    }
}