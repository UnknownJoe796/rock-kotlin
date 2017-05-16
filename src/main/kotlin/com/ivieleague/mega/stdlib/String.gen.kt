package com.ivieleague.mega.stdlib

fun main(vararg args: String) {
    Analyzer.apply {
        println(toMega("mega.string", String::class, String::plus))
    }
}