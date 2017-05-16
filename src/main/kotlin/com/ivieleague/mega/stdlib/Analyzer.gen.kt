package com.ivieleague.mega.stdlib

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import kotlin.reflect.*
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.jvmErasure

/**
 * Scans a class to create associated text
 * Created by josep on 4/22/2017.
 */

object Analyzer {

    val nameOverrides = HashMap<Any, String>().also {
        it[Byte::class] = "mega.integer.signed.1"
        it[Short::class] = "mega.integer.signed.2"
        it[Int::class] = "mega.integer.signed.4"
        it[Long::class] = "mega.integer.signed.8"
        it[Float::class] = "mega.float.4"
        it[Double::class] = "mega.float.8"
        it[Char::class] = "mega.character"
        it[String::class] = "mega.string"
        it[Boolean::class] = "mega.boolean"
        it[Math::class.java] = "mega.math"
    }

    val KClass<*>.megaName: String
        get() = this@Analyzer.nameOverrides[this] ?: this.qualifiedName ?: "?"
    val Class<*>.megaName: String
        get() = this@Analyzer.nameOverrides[this] ?: this.name ?: "?"
    val KCallable<*>.megaName: String
        get() = this@Analyzer.nameOverrides[this] ?: this.name ?: "?"
    val KParameter.megaName: String?
        get() = this@Analyzer.nameOverrides[this] ?: this.name
    val Method.megaName: String
        get() = this@Analyzer.nameOverrides[this] ?: this.name ?: "?"
    val Parameter.megaName: String?
        get() = this@Analyzer.nameOverrides[this] ?: this.name

    fun toMega(prefix: String, clazz: KClass<*>, func: KFunction<*>): String {
        val funcName = func.megaName
        val kotlinType = func.parameters.first().type.jvmErasure.qualifiedName
        val parameterString = func.parameters.drop(1).joinToString {
            "it.execute(\"${it.megaName ?: "this"}\") as ${it.type.jvmErasure.qualifiedName}"
        }
        return "functions[\"$prefix.$funcName\"] = StandardFunction {\n" +
                "    (it.execute(\"this\") as $kotlinType).${func.name}($parameterString)\n" +
                "}"

    }

    fun toMega(prefix: String, clazz: Class<*>, func: Method): String {
        if (Modifier.isStatic(func.modifiers)) {
            val funcName = func.megaName
            val parameterString = func.parameters.joinToString {
                "it.execute(\"${it.megaName ?: "this"}\") as ${it.type.kotlin.qualifiedName}"
            }
            return "functions[\"$prefix.$funcName\"] = StandardFunction {\n" +
                    "    ${clazz.name}.${func.name}($parameterString)\n" +
                    "}"
        } else {
            val funcName = func.megaName
            val receiverType = clazz.kotlin.qualifiedName
            val parameterString = func.parameters.joinToString {
                "it.execute(\"${it.megaName ?: "this"}\") as ${it.type.kotlin.qualifiedName}"
            }
            return "functions[\"$prefix.$funcName\"] = StandardFunction {\n" +
                    "    (it.execute(\"this\") as $receiverType).${func.name}($parameterString)\n" +
                    "}"
        }
    }

    fun toMega(type: KClass<*>): String {
        val builder = StringBuilder()
        try {
            for (func in type.functions) {
                builder.appendln(toMega(type.megaName, type, func))
            }
        } catch(e: KotlinReflectionInternalError) {
            for (func in type.java.declaredMethods) {
                builder.appendln(toMega(type.megaName, type.java, func))
            }
        }
        return builder.toString()
    }

    fun toMega(type: Class<*>): String {
        val builder = StringBuilder()
        for (func in type.declaredMethods) {
            builder.appendln(toMega(type.megaName, type, func))
        }
        return builder.toString()
    }
}

fun main(vararg args: String) {
    Analyzer.apply {
        val types = listOf(
                kotlin.Long::class,
                Short::class,
                Int::class,
                Long::class,
                Float::class,
                Double::class,
                Char::class,
                String::class
        )
        val javaTypes = listOf(
                Math::class.java
        )
        for (type in types) {
            println(toMega(type))
        }
        for (type in javaTypes) {
            println(toMega(type))
        }
    }
}