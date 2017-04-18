package com.ivieleague.mega.old.stdlib

import com.ivieleague.mega.old.AbstractCallBuilder

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardDebug() {
    "debug" - abstract {
        "print" - interpretation {
            println(it.mutateContext().mutateKey("value").mutateInvoke())
        }
        "info" - interpretation {
            it.mutateContext().mutateKey("value").debugInfo()
        }
    }
}