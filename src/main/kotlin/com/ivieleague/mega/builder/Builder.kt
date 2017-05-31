package com.ivieleague.mega.builder

import com.ivieleague.mega.InterpretationInterface
import com.ivieleague.mega.Root
import com.ivieleague.mega.SimpleInterpreter
import com.ivieleague.mega.SubRef

fun InterpretationInterface.execute(key: String) = quickResolveKey(key).execute()
fun InterpretationInterface.execute(index: Int) = quickResolveIndex(index).execute()
fun <T> InterpretationInterface.executeSequence(key: String) = quickResolveKey(key).let { list ->
    (0..list.call().items.size - 1).asSequence().map { list.execute(it) as T }
}

fun Root.executeMain() = SimpleInterpreter(this.calls["main"]!!, subRef = SubRef.Key("main"), root = this, parent = null).execute()