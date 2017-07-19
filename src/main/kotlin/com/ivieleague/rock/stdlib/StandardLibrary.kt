package com.ivieleague.rock.stdlib

import com.ivieleague.rock.Call
import com.ivieleague.rock.Function
import com.ivieleague.rock.Root
import com.ivieleague.rock.StandardFunction

object StandardLibrary : Root {
    override val functions = LinkedHashMap<String, Function>()
    override val calls = LinkedHashMap<String, Call>()

    init {
        functions[""] = StandardFunction { }
        functions["LIST"] = StandardFunction { }
        functions["execute"] = StandardFunction {
            it.quickResolveKey("base").addedArguments(it.quickResolveKey("arguments")).execute()
        }
        numbers()
        string()
        boolean()
        void()
        pointer()
        controls()
        debug()
        reflection()
        array()
        function()
    }

}