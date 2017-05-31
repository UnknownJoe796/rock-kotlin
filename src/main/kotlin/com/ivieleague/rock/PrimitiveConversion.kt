package com.ivieleague.rock

/**
 * Converts to/from Json
 * Created by josep on 5/3/2017.
 */
object PrimitiveConversion {
    const val INDICATOR_FUNCTION = '='
    const val INDICATOR_LANGUAGE = '/'
    const val INDICATOR_REFERENCE = '='
    const val INDICATOR_CHILD = '.'
    const val INDICATOR_INDEX = '#'
    const val INDICATOR_ROOT = '/'
    const val INDICATOR_LABEL = '@'

    const val INDICATOR_FUNCTION_STRING = INDICATOR_FUNCTION.toString()
    const val INDICATOR_LANGUAGE_STRING = INDICATOR_LANGUAGE.toString()
    const val INDICATOR_REFERENCE_STRING = INDICATOR_REFERENCE.toString()
    const val INDICATOR_CHILD_STRING = INDICATOR_CHILD.toString()
    const val INDICATOR_INDEX_STRING = INDICATOR_INDEX.toString()
    const val INDICATOR_ROOT_STRING = INDICATOR_ROOT.toString()
    const val INDICATOR_LABEL_STRING = INDICATOR_LABEL.toString()

    const val LITERAL_VOID = "rock.void.literal"
    const val LITERAL_STRING = "rock.string.literal"
    const val LITERAL_FLOAT = "rock.float.8.literal"
    const val LITERAL_INTEGER = "rock.integer.signed.4.literal"
    const val LITERAL_TRUE = "rock.boolean.true"
    const val LITERAL_FALSE = "rock.boolean.false"
    const val LITERAL_LIST = "LIST"

    fun Reference.toPrimitive(): Any? {
        return when (this) {
            is Reference.RCall -> this.call.toPrimitive()
            is Reference.RLabel -> INDICATOR_REFERENCE_STRING + INDICATOR_LABEL + this.label + this.children.toRefString()
            is Reference.RArgument -> INDICATOR_REFERENCE_STRING + this.children.toRefString()
            is Reference.RStatic -> INDICATOR_REFERENCE_STRING + INDICATOR_ROOT + this.key + this.children.toRefString()
            is Reference.RVirtualCall -> INDICATOR_REFERENCE_STRING + INDICATOR_FUNCTION
        }
    }

    fun Call.toPrimitive(): Any? {
        return when (this.function) {
            LITERAL_VOID -> null
            LITERAL_STRING -> this.literal
            LITERAL_FLOAT -> this.literal
            LITERAL_INTEGER -> this.literal
            LITERAL_TRUE -> true
            LITERAL_FALSE -> false
            LITERAL_LIST -> this.items.map { it.toPrimitive() }
            else -> LinkedHashMap<String, Any?>().also {
                it[INDICATOR_FUNCTION_STRING] = this.function
                if (this.label != null)
                    it[INDICATOR_LABEL_STRING] = this.label
                if (this.language != null)
                    it[INDICATOR_LANGUAGE_STRING] = this.language
                for ((key, value) in this.arguments) {
                    it[key] = value.toPrimitive()
                }
            }
        }
    }

    fun Function.toPrimitive(): Map<String, Any?> {
        return LinkedHashMap<String, Any?>().also {
            for ((key, value) in this.executions) {
                it[INDICATOR_LANGUAGE_STRING + key] = value.toPrimitive()
            }
            for ((key, value) in this.arguments) {
                it[key] = value.toPrimitive()
            }
        }
    }

    fun Root.toPrimitive(): Map<String, Any?> {
        return LinkedHashMap<String, Any?>().also {
            for ((key, value) in functions) {
                it[INDICATOR_FUNCTION_STRING + key] = value.toPrimitive()
            }
            for ((key, value) in calls) {
                it[key] = value.toPrimitive()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun Any?.toRoot(): Root = if (this is Map<*, *>)
        (this as Map<String, Any?>).toRoot()
    else
        throw IllegalArgumentException()

    fun Map<String, Any?>.toRoot(): Root = StandardRoot().also {
        for ((key, value) in this) {
            if (key[0] == INDICATOR_FUNCTION) {
                it.functions[key.drop(1)] = value.toFunction()
            } else {
                it.calls[key] = value.toCall()
            }
        }
    }

    fun Any?.toRef(): Reference {
        return if (this is String && this.startsWith(INDICATOR_REFERENCE)) {
            when (this[1]) {
                INDICATOR_ROOT -> {
                    val refOnly = this.substring(2)
                    val split = refOnly.indexOf(INDICATOR_CHILD, INDICATOR_INDEX)
                    if (split != -1) {
                        Reference.RStatic(refOnly.substring(0, split), refOnly.substring(split).toSubRefs())
                    } else {
                        Reference.RStatic(refOnly, listOf())
                    }
                }
                INDICATOR_LABEL -> {
                    val start = this.indexOf(INDICATOR_CHILD, INDICATOR_INDEX, startIndex = 2)
                    if (start == -1) {
                        Reference.RLabel(this.substring(2), listOf())
                    } else {
                        Reference.RLabel(this.substring(2, start), this.substring(start).toSubRefs())
                    }
                }
                INDICATOR_CHILD -> {
                    Reference.RArgument(this.substring(1).toSubRefs())
                }
                INDICATOR_INDEX -> {
                    Reference.RArgument(this.substring(1).toSubRefs())
                }
                else -> {
                    val refOnly = this.substring(1)
                    val split = refOnly.indexOf(INDICATOR_CHILD, INDICATOR_INDEX)
                    if (split != -1) {
                        Reference.RStatic(refOnly.substring(0, split), refOnly.substring(split).toSubRefs())
                    } else {
                        Reference.RStatic(refOnly, listOf())
                    }
                }
            }
        } else {
            Reference.RCall(this.toCall())
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun Any?.toFunction(): Function = if (this is Map<*, *>)
        (this as Map<String, Any?>).toFunction()
    else
        throw IllegalArgumentException()

    fun Map<String, Any?>.toFunction(): Function = StandardFunction().also {
        for ((key, value) in this) {
            if (key[0] == INDICATOR_LANGUAGE) {
                it.executions[key.drop(1)] = value.toCall()
            } else {
                it.arguments[key] = value.toRef()
            }
        }
    }

    fun Any?.toCall(): Call = when (this) {
        null -> StandardCall(LITERAL_VOID)
        true -> StandardCall(LITERAL_TRUE, literal = true)
        false -> StandardCall(LITERAL_FALSE, literal = false)
        is Int -> StandardCall(LITERAL_INTEGER, literal = this)
        is Float -> StandardCall(LITERAL_FLOAT, literal = this.toDouble())
        is Double -> StandardCall(LITERAL_FLOAT, literal = this)
        is String -> StandardCall(LITERAL_STRING, literal = this)
        is List<*> -> StandardCall(LITERAL_LIST).also {
            it.items.addAll(this@toCall.asSequence().map { it.toRef() })
        }
        is Map<*, *> -> StandardCall(this[INDICATOR_FUNCTION.toString()] as String).also {
            for ((key, value) in this) {
                when (key as String) {
                    INDICATOR_FUNCTION_STRING -> {
                    }
                    INDICATOR_LANGUAGE_STRING -> {
                        it.language = value as String
                    }
                    INDICATOR_LABEL_STRING -> {
                        it.label = value as String
                    }
                    else -> {
                        it.arguments[key] = value.toRef()
                    }
                }
            }
        }
        else -> throw IllegalStateException()
    }

    fun List<SubRef>.toRefString() = joinToString("") {
        when (it) {
            is SubRef.Index -> INDICATOR_INDEX_STRING + it.index.toString()
            is SubRef.Key -> INDICATOR_CHILD + it.key
        }
    }

    fun String.toSubRefs(): List<SubRef> {
        if (this.length == 1) return listOf() //It's an empty one
        var identifierStart = 0
        var nextIsIndex = false
        val list = ArrayList<SubRef>()
        for (index in this.indices) {
            val char = this[index]
            when (char) {
                INDICATOR_CHILD -> {
                    val identifier = this.substring(identifierStart, index)
                    if (identifier.isNotBlank()) {
                        if (nextIsIndex) {
                            list.add(SubRef.Index(identifier.toInt()))
                        } else {
                            list.add(SubRef.Key(identifier))
                        }
                    }
                    identifierStart = index + 1
                    nextIsIndex = false
                }
                INDICATOR_INDEX -> {
                    val identifier = this.substring(identifierStart, index)
                    if (identifier.isNotBlank()) {
                        if (nextIsIndex) {
                            list.add(SubRef.Index(identifier.toInt()))
                        } else {
                            list.add(SubRef.Key(identifier))
                        }
                    }
                    identifierStart = index + 1
                    nextIsIndex = true
                }
                else -> {
                }
            }
        }
        val identifier = this.substring(identifierStart)
        if (nextIsIndex) {
            list.add(SubRef.Index(identifier.toInt()))
        } else {
            list.add(SubRef.Key(identifier))
        }
        return list
    }

    fun String.indexOf(vararg options: Char, startIndex: Int = 0): Int {
        return options.asSequence().map { this.indexOf(it, startIndex) }.min()!!
    }
}