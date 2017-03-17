package com.ivieleague.mega

interface Call {
    val prototype: Ref?
    val children: Map<String, Ref>
    val list: List<Ref>

    val label: String?

    val language: String?
    val literal: Any?
    val invocation: ((CallRealization) -> Any?)?

    companion object {
        const val LANGUAGE_DEFAULT = "/default"
        const val LANGUAGE_INTERPRET = "/interpret"
    }
}

val Call.abstract: Boolean get() = language == null
fun Call.invokeAsRoot() = TrackingCallRealization(this).mutateInvoke()
