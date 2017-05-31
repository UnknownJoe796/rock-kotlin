package com.ivieleague.rock.stdlib

import com.ivieleague.rock.StandardFunction
import com.ivieleague.rock.builder.execute


fun StandardLibrary.debug() {
    functions["rock.debug.print"] = StandardFunction { println(it.execute("value")) }
}