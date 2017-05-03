package com.ivieleague.mega

/**
 * Converts to/from Json
 * Created by josep on 5/3/2017.
 */
object JSON {
    const val INDICATOR_FUNCTION = '*'
    const val INDICATOR_LANGUAGE = '/'
    const val INDICATOR_REFERENCE = '='
    const val INDICATOR_CHILD = '.'
    const val INDICATOR_INDEX = '#'
    const val INDICATOR_ROOT = '/'
    const val INDICATOR_LABEL = '@'

    const val LITERAL_VOID = "mega.void.literal"
    const val LITERAL_STRING = "mega.string.literal"
    const val LITERAL_FLOAT = "mega.float.8.literal"
    const val LITERAL_INTEGER = "mega.integer.signed.4.literal"
    const val LITERAL_TRUE = "mega.boolean.true"
    const val LITERAL_FALSE = "mega.boolean.false"
    const val LITERAL_LIST = "LIST"


    fun Any?.toRef(): Reference {
        return if (this is String && this.startsWith(INDICATOR_REFERENCE)) {
            when (this[1]) {
                INDICATOR_ROOT -> {
                    Reference.RStatic(this.substring(2).toSubRefs())
                }
                INDICATOR_LABEL -> {
                    val start = this.indexOf(INDICATOR_CHILD, startIndex = 2)
                    if (start == -1) {
                        Reference.RLabel(this.substring(2), listOf())
                    } else {
                        Reference.RLabel(this.substring(2, start), this.substring(start + 1).toSubRefs())
                    }
                }
                INDICATOR_CHILD -> {
                    Reference.RArgument(this.substring(2).toSubRefs())
                }
                else -> {
                    Reference.RStatic(this.substring(1).toSubRefs())
                }
            }
        } else {
            Reference.RCall(this.toCall())
        }
    }

    fun Any?.toCall(): Call {
        return when (this) {
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
//            is Map<*> -> StandardCall()
            else -> throw IllegalStateException()
//            null -> LiteralCall(defaultLanguage, JSON.LiteralReferences.void, null)
//            true -> NormalCall(language = defaultLanguage, prototype = JSON.LiteralReferences.booleanTrue)
//            false -> NormalCall(language = defaultLanguage, prototype = JSON.LiteralReferences.booleanFalse)
//            is Int -> LiteralCall(defaultLanguage, JSON.LiteralReferences.integer, item)
//            is Float -> LiteralCall(defaultLanguage, JSON.LiteralReferences.float, item.toDouble())
//            is Double -> LiteralCall(defaultLanguage, JSON.LiteralReferences.float, item)
//            is String -> LiteralCall(defaultLanguage, JSON.LiteralReferences.string, item)
//            is List<*> -> {
//                ListCall(item.mapIndexed { i, it -> JSON.toRef(com.ivieleague.mega.old.SubRef.Index(i), it, defaultLanguage) })
//            }
//            is Map<*, *> -> {
//                val label: String? = item[JSON.KEY_LABEL] as? String
//                val language: String? = item[JSON.KEY_LANGUAGE] as? String ?: defaultLanguage
//                val prototype: Ref? = item[JSON.KEY_PROTOTYPE]?.let { JSON.toRef(com.ivieleague.mega.old.SubRef.Key(JSON.KEY_PROTOTYPE), it, language) }
//                val children = LinkedHashMap<String, Ref>()
//                for ((key, value) in item) {
//                    when (key) {
//                        !is String -> throw IllegalArgumentException()
//                        JSON.KEY_PROTOTYPE -> {
//                        }
//                        JSON.KEY_LANGUAGE -> {
//                        }
//                        JSON.KEY_LABEL -> {
//                        }
//                        else -> children[key] = JSON.toRef(com.ivieleague.mega.old.SubRef.Key(key), value, language)
//                    }
//                }
//                NormalCall(label, language, null, prototype, children)
//            }
//            else -> throw IllegalStateException()
        }
    }

    fun String.toSubRefs(): List<SubRef> {
        var identifierStart = 0
        var nextIsIndex = false
        val list = ArrayList<SubRef>()
        for (index in this.indices) {
            val char = this[index]
            when (char) {
                INDICATOR_CHILD -> {
                    val identifier = this.substring(identifierStart, index)
                    if (nextIsIndex) {
                        list.add(SubRef.Index(identifier.toInt()))
                    } else {
                        list.add(SubRef.Key(identifier))
                    }
                    identifierStart = index + 1
                    nextIsIndex = false
                }
                INDICATOR_INDEX -> {
                    val identifier = this.substring(identifierStart, index)
                    if (nextIsIndex) {
                        list.add(SubRef.Index(identifier.toInt()))
                    } else {
                        list.add(SubRef.Key(identifier))
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
}