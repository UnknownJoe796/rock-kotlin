package com.ivieleague.mega.stdlib

import com.ivieleague.mega.*
import java.util.*
import kotlin.collections.set

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardMeta() {
    "meta" - abstract {
        "exists" - interpretation {
            it.mutateContext().copy().mutateExists("ref")
        }

        "forEach" - abstract {

            data class ForEachData(var ref: CallRealization, var index: Int) {
                fun get(): CallRealization = ref[index]
            }

            val datas = HashMap<Call, ForEachData>()

            invocation = {
                val context = it.mutateContext()
//                println("INIT: ${context.call}")
                val statement = context.get("statement")
                val ref = context.get("ref")
                val data = ForEachData(
                        ref,
                        0
                )
//                println("SETUP: ${context.call}")
                datas[context.call] = data
                for (index in ref.call.list.indices) {
                    data.index = index
                    statement.invoke()
                }
//                println("KILL: ${context.call}")
                datas.remove(context.call)
                Unit
            }

            "item" - changing(SubRef.Key("item")) {
//                println("GET: ${it.context}")
                datas[it.mutateContext().call]!!.get()
            }

            "index" - interpretation {
                datas[it.mutateContext().mutateKey("forEach").call]!!.index
            }
        }
    }
}