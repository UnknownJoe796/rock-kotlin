package com.ivieleague.mega

interface InterpretationInterface {
    fun execute(reference: Reference): Any?
    fun identify(reference: Reference): Int
    fun literal(): Any?
}