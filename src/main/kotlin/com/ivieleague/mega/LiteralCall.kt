package com.ivieleague.mega

class LiteralCall(
        override val language: String?,
        override val prototype: Ref?,
        override val literal: Any?
) : Call {
    override val children: Map<String, Ref>
        get() = mapOf()
    override val list: List<Ref>
        get() = listOf()
    override val label: String?
        get() = null
    override val invocation: ((CallRealization) -> Any?)?
        get() = null
}