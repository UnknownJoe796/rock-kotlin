package com.ivieleague.mega.old

class ListCall(
        override val list: List<Ref>
) : Call {
    override val prototype: Ref?
        get() = null
    override val children: Map<String, Ref>
        get() = mapOf()
    override val label: String?
        get() = null
    override val language: String?
        get() = null
    override val literal: Any?
        get() = null
    override val invocation: ((CallRealization) -> Any?)?
        get() = null
}