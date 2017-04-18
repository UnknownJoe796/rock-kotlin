package com.ivieleague.mega.old

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

/**
 * \n * Created by josep on 2/15/2017.
 */
fun main(args: Array<out String>) {
    val root = DSL {
        "literal" - interpretation { it.mutateContext().call.literal }
        "plus" - interpretation {
            val context = it.mutateContext()
            context.getAndRun<Double>("left") + context.getAndRun<Double>("right")
        }
        "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
            "left" - literal(root("literal"), 1.0)
            "right" - literal(root("literal"), 2.0)
        }
    }
    println(TrackingCallRealization(root).getAndRun<Double>("main"))
    val mapper = ObjectMapper(YAMLFactory())
    val ser = mapper.writeValueAsString(JSON.fromCall(root))
    println(ser)
    println(mapper.writeValueAsString(JSON.fromCall(JSON.toCall(mapper.readValue(ser, Map::class.java)))))
}