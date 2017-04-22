package com.ivieleague.mega

interface Call {
    val function: String
    val language: String?
    val label: String?
    val literal: Any?

    val arguments: Map<String, Reference>
    val items: List<Reference>
}