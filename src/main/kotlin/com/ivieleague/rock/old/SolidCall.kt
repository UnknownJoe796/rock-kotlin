package com.ivieleague.rock.old

class SolidCall(
        override val label: String? = null,
        override val language: String? = null,
        override val invocation: ((CallRealization) -> Any?)? = null,
        override val prototype: Ref? = null,
        override val children: Map<String, Ref> = mapOf(),
        override val list: List<Ref>,
        override val literal: Any?
) : Call {
    override fun toString(): String {
        return super.toString() + "." + label.toString()
    }
}

fun Call.solidify(): Call = when (this) {
    is NormalCall -> this
    is ListCall -> this
    is LiteralCall -> this
    else -> SolidCall(
            label = this.label,
            language = this.language,
            invocation = this.invocation,
            prototype = this.prototype,
            children = this.children.mapValues<String, Ref, Ref> {
                val ref = it.value
                if (ref is Ref.Definition)
                    Ref.Definition(ref.subref, ref.call.solidify())
                else ref
            },
            list = this.list.map { ref ->
                if (ref is Ref.Definition)
                    Ref.Definition(ref.subref, ref.call.solidify())
                else ref
            },
            literal = this.literal
    )
}