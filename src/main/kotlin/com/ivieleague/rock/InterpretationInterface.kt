package com.ivieleague.rock

interface InterpretationInterface {
    fun resolve(subRef: SubRef): InterpretationInterface
    fun quickResolveKey(key: String): InterpretationInterface = resolve(SubRef.Key(key))
    fun quickResolveIndex(index: Int): InterpretationInterface = resolve(SubRef.Index(index))

    fun call(): Call
    fun execute(): Any?
    fun key(): String

    fun addedArguments(interpretationInterface: InterpretationInterface): InterpretationInterface
}