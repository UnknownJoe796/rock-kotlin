package com.ivieleague.mega.stdlib

import com.ivieleague.mega.Call
import com.ivieleague.mega.Function
import com.ivieleague.mega.Root

object StandardLibrary : Root {
    override val functions = HashMap<String, Function>()
    override val calls = HashMap<String, Call>()

    init {
        numbers()
    }

}