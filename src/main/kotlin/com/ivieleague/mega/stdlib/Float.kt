package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder
import com.ivieleague.mega.getAndRun
import com.ivieleague.mega.interpretationContext
import com.ivieleague.mega.invokeList

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardFloat() {
    "float" - abstract {
        "2" - abstract {
            "literal" - interpretation { it.mutateContext().call.literal as Float }
            "sum" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Float>().fold(0f) { total, item ->
                    total + item
                }
            }
            "product" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Float>().fold(1f) { total, item ->
                    total * item
                }
            }
            "subtract" - interpretationContext {
                it.getAndRun<Float>("left") - it.getAndRun<Float>("right")
            }
            "divide" - interpretationContext {
                it.getAndRun<Float>("left") / it.getAndRun<Float>("right")
            }
            "modulus" - interpretationContext {
                it.getAndRun<Float>("left") % it.getAndRun<Float>("right")
            }
            "negative" - interpretationContext {
                -it.getAndRun<Float>("value")
            }
            "absolute" - interpretationContext {
                Math.abs(it.getAndRun<Float>("value"))
            }
            "equal" - interpretationContext {
                it.getAndRun<Float>("left") == it.getAndRun<Float>("right")
            }
            "compare" - interpretationContext {
                it.getAndRun<Float>("larger") > it.getAndRun<Float>("smaller")
            }
        }
        "4" - abstract {
            "literal" - interpretation { it.mutateContext().call.literal as Float }
            "sum" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Float>().fold(0f) { total, item ->
                    total + item
                }
            }
            "product" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Float>().fold(1f) { total, item ->
                    total * item
                }
            }
            "subtract" - interpretationContext {
                it.getAndRun<Float>("left") - it.getAndRun<Float>("right")
            }
            "divide" - interpretationContext {
                it.getAndRun<Float>("left") / it.getAndRun<Float>("right")
            }
            "modulus" - interpretationContext {
                it.getAndRun<Float>("left") % it.getAndRun<Float>("right")
            }
            "negative" - interpretationContext {
                -it.getAndRun<Float>("value")
            }
            "absolute" - interpretationContext {
                Math.abs(it.getAndRun<Float>("value"))
            }
            "equal" - interpretationContext {
                it.getAndRun<Float>("left") == it.getAndRun<Float>("right")
            }
            "compare" - interpretationContext {
                it.getAndRun<Float>("larger") > it.getAndRun<Float>("smaller")
            }
        }
        "8" - abstract {
            "literal" - interpretation { it.mutateContext().call.literal as Double }
            "sum" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Double>().fold(0.0) { total, item ->
                    total + item
                }
            }
            "product" - interpretation {
                it.mutateContext().mutateKey("values").invokeList<Double>().fold(1.0) { total, item ->
                    total * item
                }
            }
            "subtract" - interpretationContext {
                it.getAndRun<Double>("left") - it.getAndRun<Double>("right")
            }
            "divide" - interpretationContext {
                it.getAndRun<Double>("left") / it.getAndRun<Double>("right")
            }
            "modulus" - interpretationContext {
                it.getAndRun<Double>("left") % it.getAndRun<Double>("right")
            }
            "negative" - interpretationContext {
                -it.getAndRun<Double>("value")
            }
            "absolute" - interpretationContext {
                Math.abs(it.getAndRun<Double>("value"))
            }
            "equal" - interpretationContext {
                it.getAndRun<Double>("left") == it.getAndRun<Double>("right")
            }
            "compare" - interpretationContext {
                it.getAndRun<Double>("larger") > it.getAndRun<Double>("smaller")
            }
        }
    }
}