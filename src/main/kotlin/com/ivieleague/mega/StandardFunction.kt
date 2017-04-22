package com.ivieleague.mega

/**
 * A simple implementation of a function.
 * Created by josep on 4/22/2017.
 */
class StandardFunction(override var interpretation: ((InterpretationInterface) -> Any?)? = null) : Function {
    override val executions = HashMap<String, Call>()
    override val arguments = HashMap<String, Reference>()

    fun merge(other: Function) {
        if (interpretation == null) interpretation = other.interpretation
        arguments.merge(other.arguments)
        executions.merge(other.executions)
    }
}