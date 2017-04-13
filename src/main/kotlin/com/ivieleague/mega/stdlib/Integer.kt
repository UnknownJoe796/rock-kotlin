package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder
import com.ivieleague.mega.getAndRun
import com.ivieleague.mega.interpretationContext
import com.ivieleague.mega.invokeList

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardInteger() {
    "integer" - abstract {
        "signed" - abstract {
            "1" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Byte }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Byte>().fold(0) { total, item ->
                        total + item
                    }.toByte()
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Byte>().fold(1) { total, item ->
                        total * item
                    }.toByte()
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Byte>("left") - it.getAndRun<Byte>("right")).toByte()
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Byte>("left") / it.getAndRun<Byte>("right")).toByte()
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Byte>("left") % it.getAndRun<Byte>("right")).toByte()
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Byte>("value")).toByte()
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Byte>("value").toInt()).toByte()
                }
                "equal" - interpretationContext {
                    it.getAndRun<Byte>("left") == it.getAndRun<Byte>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Byte>("larger") > it.getAndRun<Byte>("smaller")
                }
            }
            "2" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Short }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Short>().fold(0) { total, item ->
                        total + item
                    }.toShort()
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Short>().fold(1) { total, item ->
                        total * item
                    }.toShort()
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Short>("left") - it.getAndRun<Short>("right")).toShort()
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Short>("left") / it.getAndRun<Short>("right")).toShort()
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Short>("left") % it.getAndRun<Short>("right")).toShort()
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Short>("value")).toShort()
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Short>("value").toInt()).toShort()
                }
                "equal" - interpretationContext {
                    it.getAndRun<Short>("left") == it.getAndRun<Short>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Short>("larger") > it.getAndRun<Short>("smaller")
                }
            }
            "4" - abstract {
                "literal" - interpretation {
                    it.mutateContext().call.literal as Int
                }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Int>().fold(0) { total, item ->
                        total + item
                    }
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Int>().fold(1) { total, item ->
                        total * item
                    }
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Int>("left") - it.getAndRun<Int>("right"))
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Int>("left") / it.getAndRun<Int>("right"))
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Int>("left") % it.getAndRun<Int>("right"))
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Int>("value"))
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Int>("value").toInt())
                }
                "equal" - interpretationContext {
                    it.getAndRun<Int>("left") == it.getAndRun<Int>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Int>("larger") > it.getAndRun<Int>("smaller")
                }
            }
            "8" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Long }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(0L) { total, item ->
                        total + item
                    }
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(1L) { total, item ->
                        total * item
                    }
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Long>("left") - it.getAndRun<Long>("right"))
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Long>("left") / it.getAndRun<Long>("right"))
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Long>("left") % it.getAndRun<Long>("right"))
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Long>("value"))
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Long>("value").toLong())
                }
                "equal" - interpretationContext {
                    it.getAndRun<Long>("left") == it.getAndRun<Long>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Long>("larger") > it.getAndRun<Long>("smaller")
                }
            }
        }
        "unsigned" - abstract {
            "1" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Short }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Short>().fold(0) { total, item ->
                        total + item
                    }.toShort()
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Short>().fold(1) { total, item ->
                        total * item
                    }.toShort()
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Short>("left") - it.getAndRun<Short>("right")).toShort()
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Short>("left") / it.getAndRun<Short>("right")).toShort()
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Short>("left") % it.getAndRun<Short>("right")).toShort()
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Short>("value")).toShort()
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Short>("value").toInt()).toShort()
                }
                "equal" - interpretationContext {
                    it.getAndRun<Short>("left") == it.getAndRun<Short>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Short>("larger") > it.getAndRun<Short>("smaller")
                }
            }
            "2" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Int }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Int>().fold(0) { total, item ->
                        total + item
                    }
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Int>().fold(1) { total, item ->
                        total * item
                    }
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Int>("left") - it.getAndRun<Int>("right"))
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Int>("left") / it.getAndRun<Int>("right"))
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Int>("left") % it.getAndRun<Int>("right"))
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Int>("value"))
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Int>("value").toInt())
                }
                "equal" - interpretationContext {
                    it.getAndRun<Int>("left") == it.getAndRun<Int>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Int>("larger") > it.getAndRun<Int>("smaller")
                }
            }
            "4" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Long }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(0L) { total, item ->
                        total + item
                    }
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(1L) { total, item ->
                        total * item
                    }
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Long>("left") - it.getAndRun<Long>("right"))
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Long>("left") / it.getAndRun<Long>("right"))
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Long>("left") % it.getAndRun<Long>("right"))
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Long>("value"))
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Long>("value").toLong())
                }
                "equal" - interpretationContext {
                    it.getAndRun<Long>("left") == it.getAndRun<Long>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Long>("larger") > it.getAndRun<Long>("smaller")
                }
            }
            "8" - abstract {
                "literal" - interpretation { it.mutateContext().call.literal as Long }
                "sum" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(0L) { total, item ->
                        total + item
                    }
                }
                "product" - interpretation {
                    it.mutateContext().mutateKey("values").invokeList<Long>().fold(1L) { total, item ->
                        total * item
                    }
                }
                "subtract" - interpretationContext {
                    (it.getAndRun<Long>("left") - it.getAndRun<Long>("right"))
                }
                "divide" - interpretationContext {
                    (it.getAndRun<Long>("left") / it.getAndRun<Long>("right"))
                }
                "modulus" - interpretationContext {
                    (it.getAndRun<Long>("left") % it.getAndRun<Long>("right"))
                }
                "negative" - interpretationContext {
                    (-it.getAndRun<Long>("value"))
                }
                "absolute" - interpretationContext {
                    Math.abs(it.getAndRun<Long>("value").toLong())
                }
                "equal" - interpretationContext {
                    it.getAndRun<Long>("left") == it.getAndRun<Long>("right")
                }
                "compare" - interpretationContext {
                    it.getAndRun<Long>("larger") > it.getAndRun<Long>("smaller")
                }
            }
        }
    }
}