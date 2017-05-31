package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.*
import java.util.*

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardControl() {
    "control" - abstract {
        "if" - interpretation {
            val context = it.mutateContext()
            if (context.getAndRun<Boolean>("condition")) {
                context.getAndRun<Any?>("consequent")
            } else {
                context.getAndRun<Any?>("alternative")
            }
        }
        "block" - abstract {

            class BlockInterpretedPointer(value: Any?) : InterpretedPointer {
                override var value:Any? = value
                    get(){
                        return field
                    }
                    set(value){
                        field = value
                    }
            }

            val locals = HashMap<Call, Map<Call, BlockInterpretedPointer>>()

            "variable" - abstract {
                invocation = {
                    val context = it.mutateContext()
                    val blockCall = context.copy().mutateKey("block").call
                    val varCall = context.call
                    locals[blockCall]!![varCall]!!
                }
            }
            invocation = {
                val context = it.mutateContext()
                val myLocals = context["variables"].realizationMap().associate {
                    it.second.call to BlockInterpretedPointer(it.second.mutateKey("value").mutateInvoke())
                }
                locals[context.call] = myLocals
                val result = context["statements"].invokeList<Any?>().last()
                locals.remove(context.call)
                result
            }
        }

        "loop" - abstract {

            class LoopBreak(val target: Call, val value:Any?) : Throwable()
            "break" - interpretation {
                val context = it.mutateContext()
                throw LoopBreak(context.copy().mutateKey("loop").call, context.mutateKey("value").mutateInvoke())
            }

            class LoopContinue(val target: Call) : Throwable()
            "continue" - interpretation {
                throw LoopContinue(it.mutateContext().mutateKey("loop").call)
            }

            invocation = {
                val context = it.mutateContext()
                val body = context["body"]
                var result: Any? = null
                while (true) {
                    try {
                        body.invoke()
                    } catch(e: LoopBreak) {
                        if (e.target == context.call) {
                            result = e.value
                            break
                        }
                    } catch(e: LoopContinue) {
                        if (e.target == context.call) {
                            continue
                        }
                    }
                }
                result
            }
        }
    }
}