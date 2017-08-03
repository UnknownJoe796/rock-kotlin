package com.ivieleague.rock

import com.ivieleague.generic.*
import java.io.PushbackReader
import java.io.StringReader
import java.io.StringWriter

/**
 * Created by josep on 5/3/2017.
 */
class ManualRepresentation {

    companion object {
        const val LITERAL_VOID = "rock.void.literal"
        const val LITERAL_STRING = "rock.string.literal"
        const val LITERAL_FLOAT = "rock.float.8.literal"
        const val LITERAL_INTEGER = "rock.integer.signed.4.literal"
        const val LITERAL_TRUE = "rock.boolean.true"
        const val LITERAL_FALSE = "rock.boolean.false"
        const val LITERAL_LIST = "LIST"

        const val INDICATOR_FUNCTION = '='
        const val INDICATOR_LANGUAGE = '/'
        const val INDICATOR_REFERENCE = '='
        const val INDICATOR_CHILD = '.'
        const val INDICATOR_INDEX = '#'
        const val INDICATOR_ROOT = '/'
        const val INDICATOR_LABEL = '@'
        const val INDICATOR_LAMBDA = '*'

        const val INDICATOR_FUNCTION_STRING = INDICATOR_FUNCTION.toString()
        const val INDICATOR_LANGUAGE_STRING = INDICATOR_LANGUAGE.toString()
        const val INDICATOR_REFERENCE_STRING = INDICATOR_REFERENCE.toString()
        const val INDICATOR_CHILD_STRING = INDICATOR_CHILD.toString()
        const val INDICATOR_INDEX_STRING = INDICATOR_INDEX.toString()
        const val INDICATOR_ROOT_STRING = INDICATOR_ROOT.toString()
        const val INDICATOR_LABEL_STRING = INDICATOR_LABEL.toString()
        const val INDICATOR_LAMBDA_STRING = INDICATOR_LAMBDA.toString()

        const val REF_CHARS = "@=.#/*"
    }

    /*
    Example Representation
    #alias rock.integer.signed.4.add as add
    #alias "rock.string.concat" as "concat"

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
        skipWhitespace()
        while (readCheck("import")) {
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
                        it.executions[key.drop(1)] = value
                    } else {
                        it.arguments[key] = value
                    }
                }
            }
        }
    }

    fun PushbackReader.parseReference(): Reference {
        return when (peekChar()) {
            INDICATOR_CHILD -> Reference.RArgument(readWhile { it.isLetterOrDigit() || it in ManualRepresentation.REF_CHARS }.toSubRefs())
            INDICATOR_INDEX -> Reference.RArgument(readWhile { it.isLetterOrDigit() || it in ManualRepresentation.REF_CHARS }.toSubRefs())
            INDICATOR_LAMBDA -> {
                skip(1)
                Reference.RLambdaArgument(readWhile { it.isLetterOrDigit() || it in ManualRepresentation.REF_CHARS }.toSubRefs())
            }
            INDICATOR_ROOT -> {
                skip(1)
                val rootKey = readWhile { it.isLetterOrDigit() }
                val next = peekChar()
                if (next == INDICATOR_CHILD || next == INDICATOR_INDEX) {
                    Reference.RStatic(rootKey, readWhile { it.isLetterOrDigit() || it in ManualRepresentation.REF_CHARS }.toSubRefs())
                } else {
                    Reference.RStatic(rootKey, listOf())
                }
            }
            INDICATOR_LABEL -> {
                skip(1)
                val label = readWhile { it.isLetterOrDigit() }
                val next = peekChar()
                if (next == INDICATOR_CHILD || next == INDICATOR_INDEX) {
                    Reference.RLabel(label, readWhile { it.isLetterOrDigit() || it in ManualRepresentation.REF_CHARS }.toSubRefs())
                } else {
                    Reference.RLabel(label, listOf())
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
        '`' -> parseTemplateString()
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

    fun PushbackReader.parseTemplateString(): Call {
        assert(readChar() == '`')
        val builder = StringBuilder()
        while (true) {
            builder.append(readUntil('`'))
            if (builder.last() != '\\') break
        }
        assert(readChar() == '`')
        val language = if (peekChar() == INDICATOR_LANGUAGE) {
            skip(1)
            readWhile { it.isLetterOrDigit() }
        } else null

        val templateString = builder.toString()
        val matchResultSequence = Regex("\\\$\\{([^}]+)}").findAll(templateString)
        return StandardCall("rock.string.concatenateList", language = language).apply {
            arguments["values"] = Reference.RCall(StandardCall(LITERAL_LIST).apply {
                var had = 0
                for (match in matchResultSequence) {
                    val beforeString = templateString.substring(had, match.range.start)
                    if (beforeString.isNotEmpty()) {
                        items += Reference.RCall(StandardCall(LITERAL_STRING, literal = beforeString))
                    }
                    items += PushbackReader(StringReader(match.groupValues[1]), 255).parseReference()
                    had = match.range.endInclusive + 1
                }
                val afterString = templateString.substring(had)
                if (afterString.isNotEmpty()) {
                    items += Reference.RCall(StandardCall(LITERAL_STRING, literal = afterString))
                }
            })
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
        val language = if (peekChar() == INDICATOR_LANGUAGE) {
            skip(1)
            readWhile { it.isLetterOrDigit() }
        } else null
        return StandardCall(LITERAL_STRING, literal = builder.toString(), language = language)
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
        it.function = aliasMap[it.function] ?: it.function
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
        assert(readChar() == '=', {
            "Lazy Message"
        })
        skipWhitespace()
        val reference = parseReference()
        skipWhitespace()
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
        for (aliasString in (root.calls["_aliases"]?.items ?: listOf()).mapNotNull { (it as? Reference.RCall)?.call?.literal as? String }) {
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
        write(reverseAliasMap[call.function] ?: call.function)
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
                writeReference(it.value)
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
            is Reference.RLambdaArgument -> write(INDICATOR_LAMBDA_STRING + reference.children.toRefString())
            is Reference.RStatic -> write(INDICATOR_ROOT + reference.key + reference.children.toRefString())
            is Reference.RVirtualCall -> write("*")
        }
    }

    //Other

    fun Root.toMRString(): String {
        val writer = StringWriter()
        val tab = TabWriter(writer)
        tab.writeFile(this)
        tab.flush()
        return writer.toString()
    }

    fun String.indexOf(vararg options: Char, startIndex: Int = 0): Int {
        return options.asSequence().map { this.indexOf(it, startIndex) }.min()!!
    }
}