package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.AbstractCallBuilder

internal fun AbstractCallBuilder.standardValidation() {
    "validation" - abstract {
        "check" - interpretation {
            val target = it.mutateContext().mutateKey("target")
            
            true
        }
    }
}