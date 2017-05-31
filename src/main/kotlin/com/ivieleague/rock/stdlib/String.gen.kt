package com.ivieleague.rock.stdlib

fun main(vararg args: String) {
    Analyzer.apply {
        println(toRock("rock.string", String::class, String::plus))
    }
}