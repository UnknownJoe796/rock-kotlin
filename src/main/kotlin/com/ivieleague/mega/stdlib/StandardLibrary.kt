package com.ivieleague.mega.stdlib

import com.ivieleague.mega.Call
import com.ivieleague.mega.Function
import com.ivieleague.mega.Root
import com.ivieleague.mega.StandardFunction

object StandardLibrary : Root {
    override val functions = HashMap<String, Function>()
    override val calls = HashMap<String, Call>()

    init {
        functions["LIST"] = StandardFunction { }
        numbers()
        string()
        boolean()
        void()
        pointer()
        controls()
    }

}