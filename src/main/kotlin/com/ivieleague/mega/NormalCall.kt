package com.ivieleague.mega

class NormalCall(
        override val label: String? = null,
        override val language: String? = null,
        override val invocation: ((CallRealization) -> Any?)? = null,
        override val prototype: Ref? = null,
        override val children: Map<String, Ref> = mapOf()
) : Call {
    override val list: List<Ref>
        get() = listOf()
    override val literal: Any?
        get() = null

    override fun toString(): String {
        return super.toString() + "." + label.toString()
    }
}