package com.ivieleague.mega

import com.ivieleague.generic.*
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
    val reverseAliasMap = HashMap<String, String>()


    //Parse

    fun PushbackReader.parseFile(): Root = StandardRoot().also {
        while (peekCheck("import")) {
            skip(6)
            skipWhitespace()
            val actual = readUntil { it.isWhitespace() }
            skipWhitespace()
            val alias = readUntil { it.isWhitespace() }
            aliasMap[alias] = actual
            reverseAliasMap[actual] = alias
            skipWhitespace()
        }
        it.calls["_aliases"] = StandardCall(LITERAL_LIST).also {
            it.items.addAll(aliasMap.map { Reference.RCall(StandardCall(LITERAL_STRING, literal = it.key + "/" + it.value)) })
        }

        while (!isAtEnd()) {
            skipWhitespace()
            val identifier = readUntil { it.isWhitespace() }
            skipWhitespace()
            val middle = readChar()
            skipWhitespace()
            if (middle == '=') {
                it.calls[identifier] = parseCall()
            } else {
                it.functions[identifier] = parseFunction()
            }
            skipWhitespace()
        }
    }

    fun PushbackReader.parseFunction(): Function {
        return StandardFunction().also {
            skip(1)//'('
            skipWhitespace()
            while (!isAtEnd()) {
                skipWhitespace()
                if (peekChar() == ')') {
                    skip(1)
                    break
                } else {
                    val (key, value) = parseArgument()
                    if (key.startsWith(INDICATOR_LANGUAGE)) {
                        it.executions[key] = (value as Reference.RCall).call
                    } else {
                        it.arguments[key] = value
                    }
                }
            }
        }
    }

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
                skip(1)
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
        '[' -> parseList()
//        '\'' -> parseChar()
        else -> parseCallFunction()
    }

    fun PushbackReader.parseNumber(): Call {
        val number = readUntil { it.isWhitespace() || it == ')' || it == ']' }
        if (number.contains('.') || number.endsWith('f')) {
            return StandardCall(LITERAL_FLOAT, literal = number.dropLastWhile { it == 'f' }.toDouble())
        } else {
            return StandardCall(LITERAL_INTEGER, literal = number.toInt())
        }
    }

    fun PushbackReader.parseString(): Call {
        assert(readChar() == '"')
        val builder = StringBuilder()
        while (true) {
            builder.append(readUntil('"'))
            if (builder.last() != '\\') break
        }
        assert(readChar() == '"')
        return StandardCall(LITERAL_STRING, literal = builder.toString())
    }

    fun PushbackReader.parseCallFunction(): Call = StandardCall("!").also {
        val start = readUntil('(').trim()
        val labelMarker = start.indexOf(INDICATOR_LABEL)
        val languageMarker = start.indexOf(INDICATOR_LANGUAGE)
        if (labelMarker != -1) {
            if (languageMarker != -1) {
                it.label = start.substring(0, labelMarker)
                it.function = start.substring(labelMarker + 1, languageMarker)
                it.language = start.substring(languageMarker + 1)
            } else {
                it.label = start.substring(0, labelMarker)
                it.function = start.substring(labelMarker + 1)
            }
        } else {
            if (languageMarker != -1) {
                it.function = start.substring(0, languageMarker)
                it.language = start.substring(languageMarker + 1)
            } else {
                it.function = start
            }
        }
        assert(readChar() == '(')
        skipWhitespace()
        while (true) {
            if (peekChar() == ')') {
                skip(1)
                break
            } else {
                val (key, value) = parseArgument()
                it.arguments[key] = value
            }
            skipWhitespace()
        }
    }

    fun PushbackReader.parseList(): Call = StandardCall(LITERAL_LIST).also {
        assert(readChar() == '[')
        skipWhitespace()
        while (true) {
            if (peekChar() == ']') {
                skip(1)
                break
            } else {
                it.items.add(parseReference())
            }
            skipWhitespace()
        }
    }

    fun PushbackReader.parseArgument(): Pair<String, Reference> {
        val identifier = readUntil('=').trim()
        skipWhitespace()
        assert(readChar() == '=')
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
        if (this.length == 1) return listOf() //It's an empty one
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


    //Serialize

    fun TabWriter.writeFile(root: Root) {
        for (aliasString in (root.calls["aliases"]?.items ?: listOf()).mapNotNull { (it as? Reference.RCall)?.call?.literal as? String }) {
            val split = aliasString.split('/')
            aliasMap[split[0]] = split[1]
            reverseAliasMap[split[1]] = split[0]
        }

        for ((key, function) in root.functions) {
            write(key)
            write(" - ")
            writeFunction(function)
            writeln()
        }

        for ((key, function) in root.calls) {
            write(key)
            write(" = ")
            writeCall(function)
            writeln()
        }
    }

    fun TabWriter.writeCall(call: Call) {
        if (call.arguments.isEmpty() && call.label == null && call.items.isEmpty() && call.language == null) {
            //literal
            when (call.function) {
                LITERAL_STRING -> {
                    write("\"${call.literal}\"")
                    return
                }
                LITERAL_FLOAT -> {
                    write(call.literal.toString())
                    write("f")
                    return
                }
                LITERAL_INTEGER -> {
                    write(call.literal.toString())
                    return
                }
            }
        }
        if (call.function == LITERAL_LIST) {
            write("[")
            if (call.items.isNotEmpty()) {
                writeln(1)
                writeSeparatingByLine(call.items) {
                    writeReference(it)
                }
                writeln(-1)
            }
            write("]")
            return
        }
        if (call.label != null) {
            write(call.label)
            write(INDICATOR_LABEL_STRING)
        }
        write(call.function)
        if (call.language != null) {
            write(INDICATOR_LANGUAGE_STRING)
            write(call.language)
        }
        write("(")
        if (call.arguments.isNotEmpty()) {
            writeln(1)
            writeSeparatingByLine(call.arguments.asIterable()) {
                write(it.key)
                write(" = ")
                writeReference(it.value)
            }
            writeln(-1)
        }
        write(")")
    }

    fun TabWriter.writeFunction(function: Function) {
        write("(")
        if (function.executions.isNotEmpty() || function.arguments.isNotEmpty()) {
            writeln(1)
            writeSeparatingByLine(function.arguments.asIterable()) {
                write(it.key)
                write(" = ")
                writeReference(it.value)
            }
            writeSeparatingByLine(function.executions.asIterable()) {
                write(INDICATOR_LANGUAGE_STRING)
                write(it.key)
                write(" = ")
                writeCall(it.value)
            }
            writeln(-1)
        }
        write(")")
    }

    fun TabWriter.writeReference(reference: Reference) {
        when (reference) {
            is Reference.RCall -> {
                writeCall(reference.call)
            }
            is Reference.RLabel -> write(INDICATOR_LABEL + reference.label + reference.children.toRefString())
            is Reference.RArgument -> write(reference.children.toRefString())
            is Reference.RStatic -> write(INDICATOR_ROOT + reference.key + reference.children.toRefString())
            is Reference.RVirtualCall -> write("*")
        }
    }

    //Other

    fun String.indexOf(vararg options: Char, startIndex: Int = 0): Int {
        return options.asSequence().map { this.indexOf(it, startIndex) }.min()!!
    }
}