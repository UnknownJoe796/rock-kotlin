package com.ivieleague.mega.stdlib

import com.ivieleague.mega.Call
import com.ivieleague.mega.Function
import com.ivieleague.mega.Root
import com.ivieleague.mega.StandardFunction

object StandardLibrary : Root {
    override val functions = LinkedHashMap<String, Function>()
    override val calls = LinkedHashMap<String, Call>()

    init {
        functions[""] = StandardFunction { }
        functions["LIST"] = StandardFunction { }
        numbers()
        string()
        boolean()
        void()
        pointer()
        controls()
        debug()
        reflection()
    }

}