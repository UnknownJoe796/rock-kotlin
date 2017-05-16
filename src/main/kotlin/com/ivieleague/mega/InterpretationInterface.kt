package com.ivieleague.mega

interface InterpretationInterface {
    fun resolve(subRef: SubRef): InterpretationInterface
    fun quickResolveKey(key: String): InterpretationInterface = resolve(SubRef.Key(key))
    fun quickResolveIndex(index: Int): InterpretationInterface = resolve(SubRef.Index(index))

    fun call(): Call
    fun execute(): Any?
}