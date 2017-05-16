package com.ivieleague.mega

/**
 * Standard call
 * Created by josep on 4/22/2017.
 */
class StandardCall(
        override var function: String,
        override var label: String? = null,
        override var literal: Any? = null,
        override var language: String? = null
) : Call {
    override val arguments: HashMap<String, Reference> = HashMap()
    override val items: ArrayList<Reference> = ArrayList()

    constructor(other: Call) : this(
            other.function,
            other.label,
            other.literal,
            other.language
    ) {
        arguments.putAll(other.arguments)
        items.addAll(other.items)
    }

    constructor(others: List<Call>) : this(
            function = others.first().function,
            label = null,
            literal = null,
            language = null
    ) {
        for (other in others) {
            merge(other)
        }
    }

    fun merge(other: Call) {
        if (label == null) label = other.label
        if (literal == null) literal = other.literal
        if (language == null) language = other.language
        arguments.merge(other.arguments)
    }

    override fun equals(other: Any?): Boolean = other is Call && extEquals(other)
    override fun hashCode(): Int = extHashCode()
    override fun toString(): String {
        return "Call(function = $function, label = $label, literal = $literal, language = $language, arguments = ${arguments.asIterable().joinToString { "${it.key} = ${it.value}" }}, items = ${items.joinToString()})"
    }
}