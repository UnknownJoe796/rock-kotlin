package com.ivieleague.rock.builder

import com.ivieleague.rock.InterpretationInterface
import com.ivieleague.rock.Root
import com.ivieleague.rock.SimpleInterpreter
import com.ivieleague.rock.SubRef

fun InterpretationInterface.execute(key: String) = quickResolveKey(key).execute()
fun InterpretationInterface.execute(index: Int) = quickResolveIndex(index).execute()
fun <T> InterpretationInterface.executeSequence(key: String) = quickResolveKey(key).let { list ->
    (0..list.call().items.size - 1).asSequence().map { list.execute(it) as T }
}

fun <T> InterpretationInterface.executeMapSequence(key: String) = quickResolveKey(key).let { map ->
    map.call().arguments.asSequence().map { it.key to map.execute(it.key) as T }
}

fun Root.executeMain() = SimpleInterpreter(this.calls["main"]!!, subRef = SubRef.Key("main"), root = this, parent = null).execute()