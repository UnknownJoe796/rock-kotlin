package com.ivieleague.mega

sealed class SubRef {
    class Key(val key: String) : SubRef() {
        override fun toString() = key
    }

    class Index(val index: Int) : SubRef() {
        override fun toString() = index.toString()
    }
}