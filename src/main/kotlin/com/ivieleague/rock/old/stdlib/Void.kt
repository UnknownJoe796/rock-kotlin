package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.AbstractCallBuilder

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