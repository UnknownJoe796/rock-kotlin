package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder
import com.ivieleague.mega.get
import com.ivieleague.mega.interpretationContext
import com.ivieleague.mega.invokeList

internal fun AbstractCallBuilder.standardArray() {
    "array" - abstract {
        //construct using items themselves
        "literal" - interpretation {
            val s = it.mutateContext().mutateKey("values")
            Array(s.call.list.size) {
                s[it].mutateInvoke()
            }
        }

        //construct using n copies of the items
        "make" - interpretation {
            val context = it.mutateContext()
            val item = context.copy().mutateKey("value").mutateInvoke()
            val count = (context.mutateKey("count").mutateInvoke() as Int)
            Array(count) { item }
        }

        //construct using a generator that makes n elements
        "generate" - abstract {
            //TODO
        }

        //get the pointer for an element
        "element" - interpretation {
            val context = it.mutateContext()
            val array = context.copy().mutateKey("array").mutateInvoke() as Array<Any?>
            val index = context.mutateKey("index").mutateInvoke() as Int
            object : InterpretedPointer {
                override var value: Any?
                    get() = array[index]
                    set(value) {
                        array[index] = value
                    }
            }
        }
    }
}