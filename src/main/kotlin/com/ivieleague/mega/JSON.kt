package com.ivieleague.mega

import java.util.*
import kotlin.collections.LinkedHashMap

/**
 *
 * \n * Created by josep on 2/21/2017.
 */
object JSON {

    object LiteralReferences {
        val void = root("mega", "void", "literal")
        val integer = root("mega", "integer", "signed", "4", "literal")
        val string = root("mega", "string", "literal")
        val float = root("mega", "float", "8", "literal")
        val booleanTrue = root("mega", "boolean", "true")
        val booleanFalse = root("mega", "boolean", "false")

        fun root(vararg keys: Any) = Ref.Root(keys.map {
            when (it) {
                is String -> SubRef.Key(it)
                is Int -> SubRef.Index(it)
                else -> throw IllegalArgumentException()
            }
        })
    }

    const val KEY_PROTOTYPE = "="
    const val KEY_LANGUAGE = "/"
    const val KEY_LABEL = "@"

    const val REF_START = "="
    const val REF_ROOT = '/'
    const val REF_LABEL = '@'
    const val REF_CONTEXT = '.'
    const val REF_SEPARATOR = '.'
    const val REF_INDEX = '#'
    const val REF_CHANGING = '*'

    fun fromCall(call: Call): Any? {
        return when (call) {
            is LiteralCall -> {
                when (call.prototype) {
                    LiteralReferences.void -> null
                    LiteralReferences.integer -> call.literal
                    LiteralReferences.string -> call.literal
                    LiteralReferences.float -> call.literal
                    else -> call.literal
                }
            }
            is ListCall -> call.list.map { fromRef(it) }
            is NormalCall -> {
                val map = LinkedHashMap<String, Any?>()
                if (call.prototype != null)
                    map.put(KEY_PROTOTYPE, fromRef(call.prototype!!))
                if (call.language != null)
                    map.put(KEY_LANGUAGE, call.language!!)
                if (call.label != null)
                    map.put(KEY_LABEL, call.label!!)
                map.putAll(call.children.asSequence().mapNotNull {
                    it.key to fromRef(it.value)
                })
                map
            }
            else -> {
                val map = LinkedHashMap<String, Any?>()
                if (call.prototype != null)
                    map.put(KEY_PROTOTYPE, fromRef(call.prototype!!))
                if (call.language != null)
                    map.put(KEY_LANGUAGE, call.language!!)
                if (call.label != null)
                    map.put(KEY_LABEL, call.label!!)
                map.putAll(call.children.asSequence().mapNotNull {
                    it.key to fromRef(it.value)
                })
                map
            }
        }
    }

    fun fromRef(ref: Ref): Any? {
        return when (ref) {
            is Ref.Label -> REF_START + REF_LABEL + ref.label + REF_SEPARATOR + fromSubRefs(ref.subRefs)
            is Ref.Root -> REF_START + fromSubRefs(ref.subRefs)
            is Ref.Context -> REF_START + REF_CONTEXT + fromSubRefs(ref.subRefs)
            is Ref.ChangingDefinition -> REF_START + REF_CHANGING
            is Ref.Definition -> fromCall(ref.call)
            else -> throw IllegalArgumentException()
        }
    }

    fun fromSubRefs(list: List<SubRef>): String = list.joinToString("") {
        when (it) {
            is SubRef.Key -> REF_SEPARATOR + it.key
            is SubRef.Index -> REF_INDEX + it.index.toString()
        }
    }.drop(1)

    fun toRef(subRef: SubRef, item: Any?, defaultLanguage: String? = null): Ref {
        return if (item is String && item.startsWith(REF_START)) {
            when (item[1]) {
                REF_ROOT -> {
                    Ref.Root(toSubRefs(item.substring(2)))
                }
                REF_LABEL -> {
                    val start = item.indexOf(REF_SEPARATOR, startIndex = 2)
                    if(start == -1){
                        Ref.Label(item.substring(2), listOf())
                    } else {
                        Ref.Label(item.substring(2, start), toSubRefs(item.substring(start + 1)))
                    }
                }
                REF_CONTEXT -> {
                    Ref.Context(toSubRefs(item.substring(2)))
                }
                else -> {
                    Ref.Root(toSubRefs(item.substring(1)))
                }
            }
//            Ref.Context
//            Ref.Label
//            Ref.Root
        } else {
            Ref.Definition(subRef, toCall(item, defaultLanguage))
        }
    }

    fun toSubRefs(string: String): List<SubRef> {
        var identifierStart = 0
        var nextIsIndex = false
        val list = ArrayList<SubRef>()
        for(index in string.indices){
            val char = string[index]
            when(char){
                REF_SEPARATOR -> {
                    val identifier = string.substring(identifierStart, index)
                    if(nextIsIndex){
                        list.add(SubRef.Index(identifier.toInt()))
                    } else {
                        list.add(SubRef.Key(identifier))
                    }
                    identifierStart = index + 1
                    nextIsIndex = false
                }
                REF_INDEX -> {
                    val identifier = string.substring(identifierStart, index)
                    if(nextIsIndex){
                        list.add(SubRef.Index(identifier.toInt()))
                    } else {
                        list.add(SubRef.Key(identifier))
                    }
                    identifierStart = index + 1
                    nextIsIndex = true
                }
                else -> {}
            }
        }
        val identifier = string.substring(identifierStart)
        if(nextIsIndex){
            list.add(SubRef.Index(identifier.toInt()))
        } else {
            list.add(SubRef.Key(identifier))
        }
        return list
    }

    fun toCall(item: Any?, defaultLanguage: String? = null): Call {
        return when (item) {
            null -> LiteralCall(defaultLanguage, LiteralReferences.void, null)
            true -> NormalCall(language = defaultLanguage, prototype = LiteralReferences.booleanTrue)
            false -> NormalCall(language = defaultLanguage, prototype = LiteralReferences.booleanFalse)
            is Int -> LiteralCall(defaultLanguage, LiteralReferences.integer, item)
            is Float -> LiteralCall(defaultLanguage, LiteralReferences.float, item.toDouble())
            is Double -> LiteralCall(defaultLanguage, LiteralReferences.float, item)
            is String -> LiteralCall(defaultLanguage, LiteralReferences.string, item)
            is List<*> -> {
                ListCall(item.mapIndexed { i, it -> toRef(SubRef.Index(i), it, defaultLanguage) })
            }
            is Map<*, *> -> {
                val label: String? = item[KEY_LABEL] as? String
                val language: String? = item[KEY_LANGUAGE] as? String ?: defaultLanguage
                val prototype: Ref? = item[KEY_PROTOTYPE]?.let { toRef(SubRef.Key(KEY_PROTOTYPE), it, language) }
                val children = LinkedHashMap<String, Ref>()
                for ((key, value) in item) {
                    when (key) {
                        !is String -> throw IllegalArgumentException()
                        KEY_PROTOTYPE -> {
                        }
                        KEY_LANGUAGE -> {
                        }
                        KEY_LABEL -> {
                        }
                        else -> children[key] = toRef(SubRef.Key(key), value, language)
                    }
                }
                NormalCall(label, language, null, prototype, children)
            }
            else -> throw IllegalStateException()
        }
    }


}