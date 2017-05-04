package com.ivieleague.mega

import com.ivieleague.generic.peekChar
import com.ivieleague.generic.readUntil
import com.ivieleague.generic.skipWhitespace
import java.io.PushbackReader

/**
 * Created by josep on 5/3/2017.
 */
class ManualRepresentation {

    companion object {
        const val LITERAL_VOID = "mega.void.literal"
        const val LITERAL_STRING = "mega.string.literal"
        const val LITERAL_FLOAT = "mega.float.8.literal"
        const val LITERAL_INTEGER = "mega.integer.signed.4.literal"
        const val LITERAL_TRUE = "mega.boolean.true"
        const val LITERAL_FALSE = "mega.boolean.false"
        const val LITERAL_LIST = "LIST"

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
    }

    /*
    Example Representation
    #alias mega.integer.signed.4.add as add
    #alias "mega.string.concat" as "concat"

    concat = function( argumentOrder = ["values"] )
    add = function(
        argumentOrder = ["this" "other"]
        /kotlin = concat([.this "+" .other])
        /kotlinFunc = concat([.this ".plus(" .other ")"])
        /reversePolish = concat(["+" .this " " .other])
        /kotlinSpecialSyntax = "${.this} + ${.other}"
        /kotlinFuncSpecialSyntax = "${.this}.plus(${.other})"
        /kotlinFuncSpecialSyntaxMaybe = "$this.plus($other)"
    )

    add(this = 2, other = 4)
    add(2, 4)
    label@add(2, 4)
    */

    val aliasMap = HashMap<String, String>()

    fun PushbackReader.parseReference(): Reference {
        return when (peekChar()) {
            INDICATOR_CHILD -> Reference.RArgument(readUntil { it.isWhitespace() }.toSubRefs())
            INDICATOR_INDEX -> Reference.RArgument(readUntil { it.isWhitespace() }.toSubRefs())
            INDICATOR_ROOT -> {
                skip(1)
                val refOnly = readUntil { it.isWhitespace() }
                val split = refOnly.indexOf(PrimitiveConversion.INDICATOR_CHILD, PrimitiveConversion.INDICATOR_INDEX)
                if (split != -1) {
                    Reference.RStatic(refOnly.substring(0, split), refOnly.substring(split).toSubRefs())
                } else {
                    Reference.RStatic(refOnly, listOf())
                }
            }
            INDICATOR_LABEL -> {
                val refOnly = readUntil { it.isWhitespace() }
                val start = refOnly.indexOf(PrimitiveConversion.INDICATOR_CHILD, PrimitiveConversion.INDICATOR_INDEX, startIndex = 2)
                if (start == -1) {
                    Reference.RLabel(refOnly.substring(0), listOf())
                } else {
                    Reference.RLabel(refOnly.substring(0, start), refOnly.substring(start).toSubRefs())
                }
            }
            else -> Reference.RCall(parseCall())
        }
    }

    fun PushbackReader.parseCall(): Call = when (peekChar()) {
        '-' -> parseNumber()
        in '0'..'9' -> parseNumber()
        '.' -> parseNumber()
        '"' -> parseString()
//        '\'' -> parseChar()
        else -> parseCallFunction()
    }

    fun PushbackReader.parseNumber(): Call {
        val number = readUntil { it.isWhitespace() }
        if (number.contains('.') || number.contains('f')) {
            return StandardCall(LITERAL_FLOAT, literal = number.toDouble())
        } else {
            return StandardCall(LITERAL_INTEGER, literal = number.toInt())
        }
    }

    fun PushbackReader.parseString(): Call {
        skip(1)
        val builder = StringBuilder()
        while (true) {
            builder.append(readUntil('"'))
            if (builder.last() != '\\') break
        }
        skip(1)
        return StandardCall(LITERAL_STRING, literal = builder.toString())
    }

    fun PushbackReader.parseCallFunction(): Call = StandardCall("!").also {
        val start = readUntil('(')
        val labelMarker = start.indexOf('@')
        if (labelMarker == -1) {
            it.function = aliasMap[start] ?: start
        } else {
            it.function = start.substring(labelMarker + 1).let { aliasMap[it] ?: it }
            it.label = start.substring(0, labelMarker)
        }
        skipWhitespace()
        while (true) {
            if (peekChar() == ')') {
                skip(1)
                break
            } else {
                val (key, value) = parseArgument()
                it.arguments[key] = value
            }
        }
    }

    fun PushbackReader.parseArgument(): Pair<String, Reference> {
        val identifier = readUntil('=').trim()
        skipWhitespace()
        val reference = parseReference()
        return identifier to reference
    }

    fun List<SubRef>.toRefString() = joinToString("") {
        when (it) {
            is SubRef.Index -> PrimitiveConversion.INDICATOR_INDEX_STRING + it.index.toString()
            is SubRef.Key -> PrimitiveConversion.INDICATOR_CHILD + it.key
        }
    }

    fun String.toSubRefs(): List<SubRef> {
        var identifierStart = 0
        var nextIsIndex = false
        val list = ArrayList<SubRef>()
        for (index in this.indices) {
            val char = this[index]
            when (char) {
                PrimitiveConversion.INDICATOR_CHILD -> {
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
                PrimitiveConversion.INDICATOR_INDEX -> {
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