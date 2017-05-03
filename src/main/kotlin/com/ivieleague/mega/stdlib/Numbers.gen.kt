package com.ivieleague.mega.stdlib

data class Type(
        val megaName: String,
        val kotlinName: String,
        val useEndConversion: Boolean = false
) {
    val operations = HashMap<String, String>()
}

interface Operation {
    fun make(type: Type): String
}

fun main(vararg args: String) {

    val floatTypes = listOf(
            Type("mega.float.2", "Float", true),
            Type("mega.float.4", "Float", true),
            Type("mega.float.8", "Double")
    )
    val signedTypes = floatTypes + listOf(
            Type("mega.integer.signed.1", "Byte", true),
            Type("mega.integer.signed.2", "Short", true),
            Type("mega.integer.signed.4", "Int"),
            Type("mega.integer.signed.8", "Long")
    )
    val numericalTypes = signedTypes + listOf(
            Type("mega.integer.unsigned.1", "Byte", true),
            Type("mega.integer.unsigned.2", "Short", true),
            Type("mega.integer.unsigned.4", "Int"),
            Type("mega.integer.unsigned.8", "Long")
    )
    val types = numericalTypes + listOf(
            Type("mega.string", "String")
    )

    val unaryTemplate = """((it.execute("ARG0") as KOTLIN).FUNKOTLIN())ENDCONVERSION"""
    val binaryTemplate = """(it.execute("ARG0") as KOTLIN).FUNKOTLIN((it.execute("ARG1") as KOTLIN))ENDCONVERSION"""
    val binaryBooleanOpTemplate = """((it.execute("ARG0") as KOTLIN) OP (it.execute("ARG1") as KOTLIN))"""
    val mathUnaryTemplate = """Math.FUNKOTLIN((it.execute("ARG0") as KOTLIN).toDouble())ENDCONVERSION"""
    val mathBinaryTemplate = """Math.FUNKOTLIN((it.execute("ARG0") as KOTLIN).toDouble(), (it.execute("ARG1") as KOTLIN).toDouble())ENDCONVERSION"""

    class NumericalOperation(val megaName: String, val template: String, val arguments: List<String>) : Operation {

        constructor(megaName: String, template: String, count: Int)
                : this(megaName, template, when (count) {
            1 -> listOf("value")
            2 -> listOf("left", "right")
            else -> throw IllegalArgumentException()
        })

        override fun make(type: Type): String {
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
            return "functions[\"${type.megaName}.$megaName\"] = StandardFunction { $result }"
        }
    }

    val numericalOperations = listOf(
            NumericalOperation("plus", binaryTemplate.replace("FUNKOTLIN", "plus"), 2),
            NumericalOperation("minus", binaryTemplate.replace("FUNKOTLIN", "minus"), 2),
            NumericalOperation("times", binaryTemplate.replace("FUNKOTLIN", "times"), 2),
            NumericalOperation("divide", binaryTemplate.replace("FUNKOTLIN", "div"), 2),
            NumericalOperation("remainder", binaryTemplate.replace("FUNKOTLIN", "rem"), 2),
            NumericalOperation("equal", binaryBooleanOpTemplate.replace("OP", "=="), 2),
            NumericalOperation("compare", binaryBooleanOpTemplate.replace("OP", "<"), listOf("lesser", "greater"))
    )
    val signedOperations = numericalOperations + listOf(
            NumericalOperation("negate", unaryTemplate.replace("FUNKOTLIN", "unaryMinus"), 1),
            NumericalOperation("absolute", mathUnaryTemplate.replace("FUNKOTLIN", "abs"), 1)
    )
    val floatOperations = signedOperations + listOf(
            NumericalOperation("power", mathBinaryTemplate.replace("FUNKOTLIN", "pow"), 1),
            NumericalOperation("absolute", mathUnaryTemplate.replace("FUNKOTLIN", "abs"), 1),
            NumericalOperation("ceiling", mathUnaryTemplate.replace("FUNKOTLIN", "ceil"), 1),
            NumericalOperation("floor", mathUnaryTemplate.replace("FUNKOTLIN", "floor"), 1),
            NumericalOperation("squareRoot", mathUnaryTemplate.replace("FUNKOTLIN", "sqrt"), 1),
            NumericalOperation("round", mathUnaryTemplate.replace("FUNKOTLIN", "round"), 1),
            NumericalOperation("sin", mathUnaryTemplate.replace("FUNKOTLIN", "sin"), 1),
            NumericalOperation("cos", mathUnaryTemplate.replace("FUNKOTLIN", "cos"), 1),
            NumericalOperation("tan", mathUnaryTemplate.replace("FUNKOTLIN", "tan"), 1),
            NumericalOperation("asin", mathUnaryTemplate.replace("FUNKOTLIN", "asin"), 1),
            NumericalOperation("acos", mathUnaryTemplate.replace("FUNKOTLIN", "acos"), 1),
            NumericalOperation("atan", mathUnaryTemplate.replace("FUNKOTLIN", "atan"), 1),
            NumericalOperation("log", mathUnaryTemplate.replace("FUNKOTLIN", "log"), 1),
            NumericalOperation("log10", mathUnaryTemplate.replace("FUNKOTLIN", "log10"), 1)
    )

    for (type in floatTypes) type.operations.putAll(floatOperations.associate { it.megaName to it.make(type) })
    for (type in signedTypes) type.operations.putAll(signedOperations.associate { it.megaName to it.make(type) })
    for (type in numericalTypes) type.operations.putAll(numericalOperations.associate { it.megaName to it.make(type) })

    val convertTemplate = """functions["1MEGA_to_2MEGA"] = StandardFunction{ (it.execute("value") as 1KOTLIN).to2KOTLIN() }"""

    val functions = StringBuilder()
    for (type in types) {
        for ((key, operation) in type.operations) {
            functions.appendln(operation)
        }
        functions.appendln()
        for (other in types) {
            if (type == other) continue
            functions.appendln(convertTemplate
                    .replace("1MEGA", type.megaName)
                    .replace("2MEGA", other.megaName)
                    .replace("1KOTLIN", type.kotlinName)
                    .replace("2KOTLIN", other.kotlinName)
            )
        }
        functions.appendln()
    }
    println(functions.toString())
}