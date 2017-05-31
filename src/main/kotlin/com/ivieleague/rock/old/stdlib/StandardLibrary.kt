package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.Call
import com.ivieleague.rock.old.DSL

/**
 *
 * \n * Created by josep on 2/22/2017.
 */
fun StandardLibrary(): Call = DSL {
    "rock" - abstract {
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

