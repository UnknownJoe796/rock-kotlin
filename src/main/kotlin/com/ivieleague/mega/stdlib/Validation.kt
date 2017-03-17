package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder
import com.ivieleague.mega.get
import com.ivieleague.mega.interpretationContext
import com.ivieleague.mega.invokeList

internal fun AbstractCallBuilder.standardValidation() {
    "validation" - abstract {
        "check" - interpretation {
            val target = it.mutateContext().mutateKey("target")
            
            true
        }
    }
}