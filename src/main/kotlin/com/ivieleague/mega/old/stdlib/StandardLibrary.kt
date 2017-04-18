package com.ivieleague.mega.old.stdlib

import com.ivieleague.mega.old.Call
import com.ivieleague.mega.old.DSL

/**
 *
 * \n * Created by josep on 2/22/2017.
 */
fun StandardLibrary(): Call = DSL {
    "mega" - abstract {
        standardBoolean()
        standardControl()
        standardDebug()
        standardFloat()
        standardInteger()
        standardMeta()
        standardPointer()
        standardString()
        standardType()
        standardVoid()
        standardArray()
    }
}

