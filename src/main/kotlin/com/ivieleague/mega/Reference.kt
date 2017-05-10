package com.ivieleague.mega

import com.ivieleague.generic.equalsList
import com.ivieleague.generic.hashCodeList

sealed class Reference {
    abstract fun copy(): Reference

    class RLabel(val label: String, val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RLabel(label, children)
        override fun equals(other: Any?): Boolean = other is RLabel && label == other.label && children.equalsList(other.children)
        override fun hashCode(): Int = label.hashCode() xor children.hashCodeList() + 2
    }

    class RArgument(val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RArgument(children)
        override fun equals(other: Any?): Boolean = other is RArgument && children.equalsList(other.children)
        override fun hashCode(): Int = children.hashCodeList()
    }

    class RStatic(val key: String, val children: List<SubRef>) : Reference() {
        override fun copy(): Reference = RStatic(key, children)
        override fun equals(other: Any?): Boolean = other is RStatic && key == other.key && children.equalsList(other.children)
        override fun hashCode(): Int = key.hashCode() xor children.hashCodeList() + 1
    }

    class RCall(val call: Call) : Reference() {
        override fun copy(): Reference = RCall(StandardCall(call))
        override fun equals(other: Any?): Boolean = other is RCall && call == other.call
        override fun hashCode(): Int = call.hashCode()
    }

    class RVirtualCall(val getCall: (InterpretationInterface) -> Call) : Reference() {
        override fun copy(): Reference = RVirtualCall(getCall)
        override fun equals(other: Any?): Boolean = other is RVirtualCall && getCall == other.getCall
        override fun hashCode(): Int = getCall.hashCode()
    }

    companion object
}