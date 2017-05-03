package com.ivieleague.mega

sealed class Reference {
    abstract fun copy(): Reference

    class RLabel(val label: String, val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RLabel(label, children)
    }

    class RArgument(val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RArgument(children)
    }

    class RCall(val call: Call) : Reference() {
        override fun copy(): Reference = RCall(StandardCall(call))
    }

    class RVirtualCall(val getCall: (InterpretationInterface) -> Call) : Reference() {
        override fun copy(): Reference = RVirtualCall(getCall)
    }

    companion object
}