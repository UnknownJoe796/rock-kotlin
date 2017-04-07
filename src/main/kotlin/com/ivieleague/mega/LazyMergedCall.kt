package com.ivieleague.mega

import com.ivieleague.generic.DeferringMap

/**
 * \n * Created by josep on 2/22/2017.
 */
class LazyMergedCall(val calls: Array<Call>) : Call {
    val callSequence = calls.asSequence()
    override val prototype: Ref? get() = callSequence.mapNotNull(Call::prototype).firstOrNull()
    override val children: Map<String, Ref> = DeferringMap(callSequence.map(Call::children))
    override val list: List<Ref> = calls.first().list
    override val label: String? = callSequence.mapNotNull(Call::label).firstOrNull()
    override val language: String? = callSequence.mapNotNull(Call::language).firstOrNull()
    override val literal: Any? = callSequence.mapNotNull(Call::literal).firstOrNull()
    override val invocation: ((CallRealization) -> Any?)? = callSequence.mapNotNull(Call::invocation).firstOrNull()
}