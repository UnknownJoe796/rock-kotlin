package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.AbstractCallBuilder
import com.ivieleague.rock.old.invokeList

internal fun AbstractCallBuilder.standardBoolean() {
    "boolean" - abstract {
        "true" - interpretation { true }
        "false" - interpretation { false }
        "not" - interpretation {
            !(it.mutateContext().mutateKey("value").mutateInvoke() as Boolean)
        }
        "and" - interpretation {
            it.mutateContext().mutateKey("values").invokeList<Boolean>().all { it }
        }
        "or" - interpretation {
            it.mutateContext().mutateKey("values").invokeList<Boolean>().any { it }
        }
        "xor" - interpretation {
            it.mutateContext().mutateKey("values").invokeList<Boolean>().count { it } % 2 == 1
        }
        "equal" - interpretation {
            val seq = it.mutateContext().mutateKey("values").invokeList<Boolean>()
            val iter = seq.iterator()
            val start = if (!iter.hasNext()) return@interpretation true
            else iter.next()
            while (iter.hasNext()) {
                if (iter.next() != start) return@interpretation false
            }
            return@interpretation true
        }
    }
}