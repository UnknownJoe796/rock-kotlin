package com.ivieleague.rock.stdlib

fun main(vararg args: String) {
    Analyzer.apply {
        println(toMega("rock.string", String::class, String::plus))
    }
}