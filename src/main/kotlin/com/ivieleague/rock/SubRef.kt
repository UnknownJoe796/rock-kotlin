package com.ivieleague.rock

sealed class SubRef {
    data class Key(val key: String) : SubRef() {
        override fun toString() = key
    }

    data class Index(val index: Int) : SubRef() {
        override fun toString() = "#" + index.toString()
    }
}