package com.ivieleague.mega

sealed class SubRef {
    class Key(val key: String) : SubRef() {
        override fun hashCode(): Int {
            return key.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return other is Key && other.key == key
        }

        override fun toString(): String = key
    }

    class Index(val index: Int) : SubRef() {
        override fun hashCode(): Int {
            return index.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return other is Index && other.index == index
        }

        override fun toString(): String = index.toString()
    }
}