package com.ivieleague.rock

sealed class Reference {
    abstract fun copy(): Reference

    data class RLabel(val label: String, val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RLabel(label, children)
        override fun toString(): String = "Label: $label Children: ${children.joinToString()}"
    }

    data class RArgument(val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RArgument(children)
        override fun toString(): String = "Argument: Children: ${children.joinToString()}"
    }

    data class RLambdaArgument(val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RLambdaArgument(children)
        override fun toString(): String = "LambdaArgument: Children: ${children.joinToString()}"
    }

    data class RStatic(val key: String, val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RStatic(key, children)
        override fun toString(): String = "Static: $key Children: ${children.joinToString()}"
    }

    data class RCall(val call: Call) : Reference() {
        override fun copy(): Reference = RCall(StandardCall(call))
        override fun toString(): String = "Call: $call"
    }

    class RVirtualCall(val getCall: (InterpretationInterface) -> Call) : Reference() {
        override fun copy(): Reference = RVirtualCall(getCall)
    }

    companion object
}