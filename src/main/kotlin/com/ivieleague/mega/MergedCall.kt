package com.ivieleague.mega

import java.util.*

/**
 * \n * Created by josep on 2/22/2017.
 */
class MergedCall(val calls: Array<Call>) : Call {
    override val prototype: Ref? = calls.asSequence().mapNotNull(Call::prototype).firstOrNull()
    override val children: Map<String, Ref> = HashMap<String, Ref>().apply {
        for (call in calls) {
            for ((key, value) in call.children) {
                val existing = this[key]
                if (existing is Ref.Definition && value is Ref.Definition) {
                    if (existing.call is MergedCall) {
                        this[key] = Ref.Definition(existing.subref, MergedCall(existing.call.calls + value.call))
                    } else {
                        this[key] = Ref.Definition(existing.subref, MergedCall(arrayOf(existing.call, value.call)))
                    }
                } else if (existing == null) {
                    this[key] = value
                }
            }
        }
    }
    override val list: List<Ref> = calls.first().list
    override val label: String? = calls.asSequence().mapNotNull(Call::label).firstOrNull()
    override val language: String? = calls.asSequence().mapNotNull(Call::language).firstOrNull()
    override val literal: Any? = calls.asSequence().mapNotNull(Call::literal).firstOrNull()
    override val invocation: ((CallRealization) -> Any?)? = calls.asSequence().mapNotNull(Call::invocation).firstOrNull()
}