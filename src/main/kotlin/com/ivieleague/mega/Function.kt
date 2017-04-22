package com.ivieleague.mega

interface Function {
    val interpretation: ((InterpretationInterface) -> Any?)?
    val executions: Map<String, Call>
    val arguments: Map<String, Reference>
}