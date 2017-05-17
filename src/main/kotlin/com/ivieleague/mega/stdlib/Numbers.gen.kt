package com.ivieleague.mega.stdlib

data class Type(
        val megaName: String,
        val kotlinName: String,
        val useEndConversion: Boolean = false
)

fun main(vararg args: String) {
    val builder = StringBuilder()
    builder.generateOperations()
    builder.generateConversions()
    println(builder.toString()
            .replace("0.toFloat()", "0f")
            .replace("1.toFloat()", "1f")
            .replace("0.toDouble()", "0.0")
            .replace("1.toDouble()", "1.0")
            .replace("0.toLong()", "0L")
            .replace("1.toLong()", "1L")
            .replace("0.toInt()", "0")
            .replace("1.toInt()", "1")
            .replace(Regex("([^a-zA-Z0-9])\\(it\\.execute\\(\"([a-zA-Z]+)\"\\) as Double\\)\\.toDouble\\(\\)"), "$1(it.execute(\"$2\") as Double)")
            .replace(Regex("([^a-zA-Z0-9])\\(it\\.execute\\(\"([a-zA-Z]+)\"\\) as Int\\)\\.toInt\\(\\)"), "$1(it.execute(\"$2\") as Int)")
    )
}

private fun Appendable.generateOperations() {
    val unaryTemplate = """((it.execute("ARG0") as KOTLIN).FUNKOTLIN())ENDCONVERSION"""
    val binaryTemplate = """(it.execute("ARG0") as KOTLIN).FUNKOTLIN((it.execute("ARG1") as KOTLIN))ENDCONVERSION"""
    val binaryBooleanOpTemplate = """((it.execute("ARG0") as KOTLIN) OP (it.execute("ARG1") as KOTLIN))"""
    val mathUnaryTemplate = """Math.FUNKOTLIN((it.execute("ARG0") as KOTLIN).toDouble())ENDCONVERSION"""
    val mathBinaryTemplate = """Math.FUNKOTLIN((it.execute("ARG0") as KOTLIN).toDouble(), (it.execute("ARG1") as KOTLIN).toDouble())ENDCONVERSION"""

    fun megaFunctionWrapper(type: Type, megaName: String, kotlin: String) = "functions[\"${type.megaName}.$megaName\"] = StandardFunction { $kotlin }"

    fun make(type: Type, megaName: String, template: String, arguments: List<String>): String {
        val result = template
                .replace("FUNMEGA", megaName)
                .replace("MEGA", type.megaName)
                .replace("KOTLIN", type.kotlinName)
                .let {
                    if (type.useEndConversion)
                        it.replace("ENDCONVERSION", ".to${type.kotlinName}()")
                    else
                        it.replace("ENDCONVERSION", "")
                }
                .let {
                    var current = it
                    arguments.forEachIndexed { index, s ->
                        current = current.replace("ARG${index}", s)
                    }
                    current
                }
        return megaFunctionWrapper(type, megaName, result)
    }

    fun make(type: Type, megaName: String, template: String, count: Int): String {
        return make(type, megaName, template, when (count) {
            1 -> listOf("value")
            2 -> listOf("left", "right")
            else -> throw IllegalArgumentException()
        })
    }

    fun numericalOperations(type: Type) {
        appendln(megaFunctionWrapper(type, "literal", """it.call().literal"""))
        appendln(make(type, "plus", binaryTemplate.replace("FUNKOTLIN", "plus"), 2))
        appendln(make(type, "minus", binaryTemplate.replace("FUNKOTLIN", "minus"), 2))
        appendln(make(type, "times", binaryTemplate.replace("FUNKOTLIN", "times"), 2))
        appendln(make(type, "divide", binaryTemplate.replace("FUNKOTLIN", "div"), 2))
        appendln(make(type, "remainder", binaryTemplate.replace("FUNKOTLIN", "rem"), 2))
        appendln(make(type, "equal", binaryBooleanOpTemplate.replace("OP", "=="), 2))
        appendln(make(type, "compare", binaryBooleanOpTemplate.replace("OP", "<"), listOf("lesser", "greater")))
        appendln(megaFunctionWrapper(type, "sum", """it.executeSequence<${type.kotlinName}>("values").fold(0.to${type.kotlinName}()){ sum, it -> (sum + it).to${type.kotlinName}() }"""))
        appendln(megaFunctionWrapper(type, "product", """it.executeSequence<${type.kotlinName}>("values").fold(1.to${type.kotlinName}()){ product, it -> (product * it).to${type.kotlinName}() }"""))
    }

    fun signedOperations(type: Type) {
        appendln(make(type, "negate", unaryTemplate.replace("FUNKOTLIN", "unaryMinus"), 1))
        appendln(make(type, "absolute", mathUnaryTemplate.replace("FUNKOTLIN", "abs"), 1))
    }

    fun floatOperations(type: Type) {
        appendln(make(type, "power", mathBinaryTemplate.replace("FUNKOTLIN", "pow"), listOf("value", "exponent", "")))
        appendln(make(type, "absolute", mathUnaryTemplate.replace("FUNKOTLIN", "abs"), 1))
        appendln(make(type, "ceiling", mathUnaryTemplate.replace("FUNKOTLIN", "ceil"), 1))
        appendln(make(type, "floor", mathUnaryTemplate.replace("FUNKOTLIN", "floor"), 1))
        appendln(make(type, "squareRoot", mathUnaryTemplate.replace("FUNKOTLIN", "sqrt"), 1))
        appendln(megaFunctionWrapper(type, "round", """Math.round(it.execute("value") as ${type.kotlinName}).to${type.kotlinName}()"""))
        appendln(make(type, "sin", mathUnaryTemplate.replace("FUNKOTLIN", "sin"), 1))
        appendln(make(type, "cos", mathUnaryTemplate.replace("FUNKOTLIN", "cos"), 1))
        appendln(make(type, "tan", mathUnaryTemplate.replace("FUNKOTLIN", "tan"), 1))
        appendln(make(type, "asin", mathUnaryTemplate.replace("FUNKOTLIN", "asin"), 1))
        appendln(make(type, "acos", mathUnaryTemplate.replace("FUNKOTLIN", "acos"), 1))
        appendln(make(type, "atan", mathUnaryTemplate.replace("FUNKOTLIN", "atan"), 1))
        appendln(make(type, "log", mathUnaryTemplate.replace("FUNKOTLIN", "log"), 1))
        appendln(make(type, "log10", mathUnaryTemplate.replace("FUNKOTLIN", "log10"), 1))
    }

    appendln("//Operations")
    appendln()

    listOf(
            Type("mega.float.2", "Float", true),
            Type("mega.float.4", "Float", true),
            Type("mega.float.8", "Double")
    ).forEach {
        appendln("//Operations for ${it.megaName}")
        floatOperations(it)
        signedOperations(it)
        numericalOperations(it)
        appendln()
    }
    listOf(
            Type("mega.integer.signed.1", "Byte", true),
            Type("mega.integer.signed.2", "Short", true),
            Type("mega.integer.signed.4", "Int"),
            Type("mega.integer.signed.8", "Long")
    ).forEach {
        appendln("//Operations for ${it.megaName}")
        signedOperations(it)
        numericalOperations(it)
        appendln()
    }
    listOf(
            Type("mega.integer.unsigned.1", "Byte", true),
            Type("mega.integer.unsigned.2", "Short", true),
            Type("mega.integer.unsigned.4", "Int"),
            Type("mega.integer.unsigned.8", "Long")
    ).forEach {
        appendln("//Operations for ${it.megaName}")
        numericalOperations(it)
        appendln()
    }
}

private fun Appendable.generateConversions() {
    appendln("//Conversions")

    val conversionTypes = listOf(
            Type("mega.float.2", "Float", true),
            Type("mega.float.4", "Float", true),
            Type("mega.float.8", "Double"),
            Type("mega.integer.signed.1", "Byte", true),
            Type("mega.integer.signed.2", "Short", true),
            Type("mega.integer.signed.4", "Int"),
            Type("mega.integer.signed.8", "Long"),
            Type("mega.integer.unsigned.1", "Byte", true),
            Type("mega.integer.unsigned.2", "Short", true),
            Type("mega.integer.unsigned.4", "Int"),
            Type("mega.integer.unsigned.8", "Long"),
            Type("mega.string", "String")
    )

    val convertTemplate = """functions["1MEGA_to_2MEGA"] = StandardFunction{ (it.execute("value") as 1KOTLIN).to2KOTLIN() }"""

    for (type in conversionTypes) {
        appendln("//Conversions from ${type.megaName}")
        for (other in conversionTypes) {
            if (type == other) continue
            appendln(convertTemplate
                    .replace("1MEGA", type.megaName)
                    .replace("2MEGA", other.megaName)
                    .replace("1KOTLIN", type.kotlinName)
                    .replace("2KOTLIN", other.kotlinName)
            )
        }
        appendln()
    }
}