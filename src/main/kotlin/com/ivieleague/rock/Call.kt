package com.ivieleague.rock

import com.ivieleague.generic.equalsList
import com.ivieleague.generic.equalsMap
import com.ivieleague.generic.hashCodeList
import com.ivieleague.generic.hashCodeMap

interface Call {
    val function: String
    val language: String?
    val label: String?
    val literal: Any?

    val arguments: Map<String, Reference>
    val items: List<Reference>
}

fun Call.extEquals(other: Call): Boolean = function == other.function &&
        language == other.language &&
        label == other.label &&
        literal == other.literal &&
        arguments.equalsMap(other.arguments) &&
        items.equalsList(other.items)

fun Call.extHashCode(): Int = (function.hashCode().shl(1)) xor
        (label?.hashCode()?.shl(2) ?: 0x0) xor
        (language?.hashCode()?.shl(3) ?: 0x0) xor
        (literal?.hashCode()?.shl(4) ?: 0x0) xor
        (items.hashCodeList() shl 2) xor
        arguments.hashCodeMap()