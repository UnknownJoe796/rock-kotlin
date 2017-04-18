package com.ivieleague.mega.old.stdlib

import com.ivieleague.mega.old.AbstractCallBuilder

internal fun AbstractCallBuilder.standardValidation() {
    "validation" - abstract {
        "check" - interpretation {
            val target = it.mutateContext().mutateKey("target")
            
            true
        }
    }
}