package com.ivieleague.mega

sealed class SubRef {
    class Key(val key: String) : SubRef() {
        override fun toString() = key
        override fun equals(other: Any?): Boolean = other is SubRef.Key && key == other.key
        override fun hashCode(): Int = key.hashCode() + 1
    }

    class Index(val index: Int) : SubRef() {
        override fun toString() = "#" + index.toString()
        override fun equals(other: Any?): Boolean = other is SubRef.Index && index == other.index
        override fun hashCode(): Int = index.hashCode() + 1
    }
}