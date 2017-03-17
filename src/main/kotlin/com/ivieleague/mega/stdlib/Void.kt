package com.ivieleague.mega.stdlib

import com.ivieleague.mega.AbstractCallBuilder

/**
 *
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardVoid() {
    "void" - abstract {
        "literal" - interpretation {
            Unit
        }
    }
}