package com.ivieleague.rock.stdlib

fun main(vararg args: String) {
    NumbersGen.run {
        val builder = StringBuilder()

        for (operation in NumbersGen.allOperations()) {
            builder.appendln(operation.toInterpreter())
        }
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
        builder.setLength(0)

        for (operation in NumbersGen.allOperations()) {
            builder.appendln(operation.toManrepKotlin())
        }
        builder.generateKotlinConversions()
        println(builder.toString()
                .replace("0.toFloat()", "0f")
                .replace("1.toFloat()", "1f")
                .replace("0.toDouble()", "0.0")
                .replace("1.toDouble()", "1.0")
                .replace("0.toLong()", "0L")
                .replace("1.toLong()", "1L")
                .replace("0.toInt()", "0")
                .replace("1.toInt()", "1")
        )
        builder.setLength(0)
    }
}

object NumbersGen {

    data class Type(
            val rockName: String,
            val kotlinName: String,
            val useEndConversion: Boolean = false
    )

    data class OperationInput(
            val argument: (String) -> String,
            val sequence: (String) -> String,
            val numericalConversion: (String) -> String
    )

    data class Operation(
            val type: Type,
            val rockName: String,
            val makeKotlinString: OperationInput.() -> String
    )

    fun Operation.toInterpreter(): String {
        val input = OperationInput(
                argument = { "(it.execute(\"$it\") as ${type.kotlinName})" },
                sequence = { "it.executeSequence<${type.kotlinName}>(\"$it\")" },
                numericalConversion = { "($it).to${type.kotlinName}()" }
        )
        return "functions[\"${type.rockName}.$rockName\"] = StandardFunction { ${makeKotlinString.invoke(input)} }"
    }

    fun Operation.toManrepKotlin(): String {
        val input = OperationInput(
                argument = { "\${.$it}" },
                sequence = { "sequenceOf(\${rock.string.join( values = .$it  separator = \", \" )})" },
                numericalConversion = { "($it).to${type.kotlinName}()" }
        )
        return """${type.rockName}.$rockName - ( /kotlin = `${makeKotlinString.invoke(input)}`/interpret )"""
    }

    fun plainOperations(type: Type) = listOf<Operation>(
            Operation(type, "plus") { numericalConversion("${argument("left")} + ${argument("right")}") },
            Operation(type, "minus") { numericalConversion("${argument("left")} - ${argument("right")}") },
            Operation(type, "times") { numericalConversion("${argument("left")} * ${argument("right")}") },
            Operation(type, "divide") { numericalConversion("${argument("left")} / ${argument("right")}") },
            Operation(type, "remainder") { numericalConversion("${argument("left")} % ${argument("right")}") },
            Operation(type, "equal") { "${argument("left")} == ${argument("right")}" },
            Operation(type, "compare") { "${argument("lesser")} < ${argument("getter")}" },
            Operation(type, "sum") { "${sequence("values")}.fold(${numericalConversion("0")}){sum, it -> ${numericalConversion("sum + it")}}" },
            Operation(type, "product") { "${sequence("values")}.fold(${numericalConversion("1")}){sum, it -> ${numericalConversion("sum + it")}}" }
    )

    fun signedOperations(type: Type) = listOf<Operation>(
            Operation(type, "negate") { "-${argument("value")}" }
    )

    fun floatOperations(type: Type) = listOf<Operation>(
            Operation(type, "power") { numericalConversion("Math.pow(${argument("value")}.toDouble(), ${argument("exponent")})") },
            Operation(type, "ceiling") { numericalConversion("Math.ceil(${argument("value")}.toDouble())") },
            Operation(type, "floor") { numericalConversion("Math.floor(${argument("value")}.toDouble())") },
            Operation(type, "round") { numericalConversion("Math.round(${argument("value")}.toDouble())") },
            Operation(type, "squareRoot") { numericalConversion("Math.sqrt(${argument("value")}.toDouble())") },
            Operation(type, "sin") { numericalConversion("Math.sin(${argument("value")}.toDouble())") },
            Operation(type, "cos") { numericalConversion("Math.cos(${argument("value")}.toDouble())") },
            Operation(type, "tan") { numericalConversion("Math.tan(${argument("value")}.toDouble())") },
            Operation(type, "asin") { numericalConversion("Math.asin(${argument("value")}.toDouble())") },
            Operation(type, "acos") { numericalConversion("Math.acos(${argument("value")}.toDouble())") },
            Operation(type, "atan") { numericalConversion("Math.atan(${argument("value")}.toDouble())") },
            Operation(type, "atan2") { numericalConversion("Math.atan2(${argument("y")}.toDouble(), ${argument("x")}.toDouble())") },
            Operation(type, "log") { numericalConversion("Math.log(${argument("value")}.toDouble())") },
            Operation(type, "log10") { numericalConversion("Math.log10(${argument("value")}.toDouble())") }
    )

    val ubyte = Type("rock.integer.unsigned.1", "Byte", true)
    val ushort = Type("rock.integer.unsigned.2", "Short", true)
    val uint = Type("rock.integer.unsigned.4", "Int")
    val ulong = Type("rock.integer.unsigned.8", "Long")
    val byte = Type("rock.integer.signed.1", "Byte", true)
    val short = Type("rock.integer.signed.2", "Short", true)
    val int = Type("rock.integer.signed.4", "Int")
    val long = Type("rock.integer.signed.8", "Long")
    val half = Type("rock.float.2", "Float", true)
    val float = Type("rock.float.4", "Float", true)
    val double = Type("rock.float.8", "Double")

    val unsignedTypes = listOf(
            byte,
            short,
            int,
            long
    )
    val signedTypes = listOf(
            ubyte,
            ushort,
            uint,
            ulong
    )
    val floatTypes = listOf(
            half,
            float,
            double
    )
    val conversionTypes = unsignedTypes + signedTypes + floatTypes + Type("rock.string", "String")

    fun allOperations(): List<Operation> = unsignedTypes.flatMap { plainOperations(it) } +
            signedTypes.flatMap { plainOperations(it) + signedOperations(it) } +
            floatTypes.flatMap { plainOperations(it) + signedOperations(it) + floatOperations(it) } +
            listOf<Operation>(
                    Operation(ubyte, "absolute") { numericalConversion("Math.abs(${argument("value")}.toInt())") },
                    Operation(ushort, "absolute") { numericalConversion("Math.abs(${argument("value")}.toInt())") },
                    Operation(byte, "absolute") { numericalConversion("Math.abs(${argument("value")}.toInt())") },
                    Operation(short, "absolute") { numericalConversion("Math.abs(${argument("value")}.toInt())") },
                    Operation(int, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(long, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(uint, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(ulong, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(half, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(float, "absolute") { numericalConversion("Math.abs(${argument("value")})") },
                    Operation(double, "absolute") { numericalConversion("Math.abs(${argument("value")})") }
            )

    fun Appendable.generateConversions() {
        appendln("//Conversions")

        val conversionTypes = listOf(
                Type("rock.float.2", "Float", true),
                Type("rock.float.4", "Float", true),
                Type("rock.float.8", "Double"),
                Type("rock.integer.signed.1", "Byte", true),
                Type("rock.integer.signed.2", "Short", true),
                Type("rock.integer.signed.4", "Int"),
                Type("rock.integer.signed.8", "Long"),
                Type("rock.integer.unsigned.1", "Byte", true),
                Type("rock.integer.unsigned.2", "Short", true),
                Type("rock.integer.unsigned.4", "Int"),
                Type("rock.integer.unsigned.8", "Long"),
                Type("rock.string", "String")
        )

        val convertTemplate = """functions["1MEGA_to_2MEGA"] = StandardFunction{ (it.execute("value") as 1KOTLIN).to2KOTLIN() }"""

        for (type in conversionTypes) {
            appendln("//Conversions from ${type.rockName}")
            for (other in conversionTypes) {
                if (type == other) continue
                appendln(convertTemplate
                        .replace("1MEGA", type.rockName)
                        .replace("2MEGA", other.rockName)
                        .replace("1KOTLIN", type.kotlinName)
                        .replace("2KOTLIN", other.kotlinName)
                )
            }
            appendln()
        }
    }

    fun Appendable.generateKotlinConversions() {
        val conversionTypes = listOf(
                Type("rock.float.2", "Float", true),
                Type("rock.float.4", "Float", true),
                Type("rock.float.8", "Double"),
                Type("rock.integer.signed.1", "Byte", true),
                Type("rock.integer.signed.2", "Short", true),
                Type("rock.integer.signed.4", "Int"),
                Type("rock.integer.signed.8", "Long"),
                Type("rock.integer.unsigned.1", "Byte", true),
                Type("rock.integer.unsigned.2", "Short", true),
                Type("rock.integer.unsigned.4", "Int"),
                Type("rock.integer.unsigned.8", "Long"),
                Type("rock.string", "String")
        )

        val convertTemplate = "1MEGA_to_2MEGA - ( /kotlin = `(\${.value}).to2KOTLIN()` )"

        for (type in conversionTypes) {
            for (other in conversionTypes) {
                if (type == other) continue
                appendln(convertTemplate
                        .replace("1MEGA", type.rockName)
                        .replace("2MEGA", other.rockName)
                        .replace("1KOTLIN", type.kotlinName)
                        .replace("2KOTLIN", other.kotlinName)
                )
            }
            appendln()
        }
    }
}