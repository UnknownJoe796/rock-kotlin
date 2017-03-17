package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder

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