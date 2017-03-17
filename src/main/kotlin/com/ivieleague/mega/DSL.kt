package com.ivieleague.mega

import java.util.*

/**
 *
 * \n * Created by josep on 2/15/2017.
 */

fun DSL(setup: AbstractCallBuilder.() -> Unit): Call {
    return AbstractCallBuilder().apply(setup).build()
}

inline fun AbstractRefContext.interpretationContext(crossinline how: (CallRealization) -> Any?)
        = AbstractCallBuilder(null).apply { invocation = {
    how(it.mutateContext())
} }.build()

interface AbstractRefContext {
    fun action(how: (CallRealization) -> Any?)
            = NormalCallBuilder(Call.LANGUAGE_INTERPRET).apply {
        invocation = how
    }.build()

    fun abstract(prototype: Ref? = null, setup: AbstractCallBuilder.() -> Unit)
            = AbstractCallBuilder(prototype).apply(setup).build()

    fun interpretation(how: (CallRealization) -> Any?)
            = AbstractCallBuilder(null).apply { invocation = how }.build()

    fun call(language: String, prototype: Ref?, setup: NormalCallBuilder.() -> Unit)
            = NormalCallBuilder(language, prototype)
            .apply(setup)
            .build()

    fun literal(language: String, prototype: Ref?, literal: Any?) =
            LiteralCall(
                    language,
                    prototype,
                    literal
            )

    fun changing(subRef: SubRef, method: (CallRealization) -> CallRealization)
            = Ref.ChangingDefinition(subRef, method)

    fun root(vararg keys: Any) = Ref.Root(keys.map {
        when (it) {
            is String -> SubRef.Key(it)
            is Int -> SubRef.Index(it)
            else -> throw IllegalArgumentException()
        }
    })

    fun label(label: String, vararg keys: Any) = Ref.Label(label, keys.map {
        when (it) {
            is String -> SubRef.Key(it)
            is Int -> SubRef.Index(it)
            else -> throw IllegalArgumentException()
        }
    })

    fun context(vararg keys: Any) = Ref.Context(keys.map {
        when (it) {
            is String -> SubRef.Key(it)
            is Int -> SubRef.Index(it)
            else -> throw IllegalArgumentException()
        }
    })
}

interface RefContext : AbstractRefContext {
    val currentLanguage: String

    fun call(prototype: Ref?, setup: NormalCallBuilder.() -> Unit)
            = NormalCallBuilder(currentLanguage, prototype)
            .apply(setup)
            .build()


    fun literal(prototype: Ref?, literal: Any?) =
            LiteralCall(
                    currentLanguage,
                    prototype,
                    literal
            )

}


open class AbstractCallBuilder(
        var prototype: Ref? = null
) : AbstractRefContext {
    open var language: String? = null

    var label: String? = null
    var invocation: ((CallRealization) -> Any?)? = null
    val children = HashMap<String, Ref>()

    operator fun String.minus(ref: Ref) {
        children[this] = ref
    }
    operator fun String.minus(call: Call) {
        children[this] = Ref.Definition(SubRef.Key(this), call)
    }

    fun build(): NormalCall = NormalCall(label, language, invocation, prototype, children)
}

class NormalCallBuilder(
        override var language: String?,
        prototype: Ref? = null
) : AbstractCallBuilder(prototype), RefContext {
    override val currentLanguage: String = language!!
}