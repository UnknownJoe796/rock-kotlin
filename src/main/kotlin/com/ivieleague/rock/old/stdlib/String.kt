package com.ivieleague.rock.old.stdlib

import com.ivieleague.rock.old.AbstractCallBuilder
import com.ivieleague.rock.old.get
import com.ivieleague.rock.old.invokeList

/**
 * \n * Created by josep on 2/23/2017.
 */
fun AbstractCallBuilder.standardString() {
    "string" - abstract {
        "concat" - interpretation {
            it.mutateContext().mutateKey("values").invokeList<String>().joinToString("")
        }
        "join" - interpretation {
            val context = it.mutateContext()
            context.get("values").invokeList<String>().joinToString(
                    context.get("separator").mutateInvoke() as String
            )
        }
        "literal" - interpretation {
            it.mutateContext().call.literal as String
        }
    }
}