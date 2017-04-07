package com.ivieleague.mega

sealed class Ref {

    abstract fun traverse(from: CallRealization): CallRealization

    class Definition(val subref: SubRef, val call: Call) : Ref() {
        override fun traverse(from: CallRealization): CallRealization {
            return from.mutateChild(subref, call)
        }

        override fun equals(other: Any?): Boolean = other is Definition && subref == other.subref && call same other.call
        override fun toString(): String = "Definition: " + subref.toString()
    }

    class ChangingDefinition(val subref: SubRef, val callGenerator: (CallRealization) -> CallRealization) : Ref() {
        override fun traverse(from: CallRealization): CallRealization = callGenerator(from)
        override fun toString(): String {
            return "Changing Definition"
        }
    }

    class Root(val subRefs: List<SubRef>) : Ref() {
        override fun traverse(from: CallRealization): CallRealization {
            return resolveSubrefs(traverseFirst(from), subRefs)
        }

        fun traverseFirst(from: CallRealization): CallRealization = from.mutateRoot()
        override fun equals(other: Any?): Boolean = other is Root && subRefs.equalsList(other.subRefs)
        override fun hashCode(): Int = subRefs.hashCodeList()
        override fun toString(): String = "/" + subRefs.joinToString(".")
    }

    class Context(val subRefs: List<SubRef>) : Ref() {
        override fun traverse(from: CallRealization): CallRealization {
            return resolveSubrefs(traverseFirst(from), subRefs)
        }

        fun traverseFirst(from: CallRealization): CallRealization = from.mutateContext()
        override fun equals(other: Any?): Boolean = other is Context && subRefs.equalsList(other.subRefs)
        override fun hashCode(): Int = subRefs.hashCodeList()
        override fun toString(): String = "." + subRefs.joinToString(".")
    }

    class Label(val label: String, val subRefs: List<SubRef>) : Ref() {
        override fun traverse(from: CallRealization): CallRealization {
            return resolveSubrefs(traverseFirst(from), subRefs)
        }

        fun traverseFirst(from: CallRealization): CallRealization = from.mutateLabel(label)
        override fun equals(other: Any?): Boolean = other is Label && subRefs.equalsList(other.subRefs) && label == other.label
        override fun hashCode(): Int = label.hashCode() + subRefs.hashCodeList()
        override fun toString(): String = "@" + label + "." + subRefs.joinToString(".")
    }

    companion object {
        fun resolveSubrefs(realization: CallRealization, subRefs: List<SubRef>): CallRealization {
            var current = realization
            for (item in subRefs) {
                current = when (item) {
                    is SubRef.Index -> current.mutateIndex(item.index)
                    is SubRef.Key -> current.mutateKey(item.key)
                }
            }
            return current
        }
    }

    fun List<SubRef>.hashCodeList(): Int = sumBy(SubRef::hashCode)
}

infix fun Call.same(other: Call): Boolean {
    return this.prototype == other.prototype &&
            this.children same other.children &&
            this.list equalsList other.list &&
            this.label == other.label &&
            this.language == other.language &&
            this.literal == other.literal &&
            this.invocation == other.invocation
}

infix fun Map<String, Ref>.same(other: Map<String, Ref>): Boolean {
    if (this.size != other.size) return false
    for (key in keys) {
        if (this[key] != other[key]) return false
    }
    return true
}

infix fun <T> List<T>.equalsList(other: List<T>): Boolean {
    if (this.size != other.size) return false
    for (index in this.indices) {
        if (this[index] != other[index]) return false
    }
    return true
}