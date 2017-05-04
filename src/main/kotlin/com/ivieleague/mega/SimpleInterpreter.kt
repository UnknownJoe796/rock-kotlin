package com.ivieleague.mega

class SimpleInterpreter(
        val call: Call,
        val parent: SimpleInterpreter?,
        val subRef: SubRef? = null,
        val language: String = call.language ?: parent?.language ?: Languages.INTERPRET,
        var arguments: SimpleInterpreter? = parent?.arguments,
        val root: Root = parent!!.root
) : InterpretationInterface {
    init {
        println(path().joinToString("."))
    }

    val function: Function = root.functions[call.function] ?: throw makeException("Function '${call.function}' not found")

    fun follow(sub: SubRef): SimpleInterpreter = when (sub) {
        is SubRef.Key -> follow(sub, call.arguments[sub.key] ?: function.arguments[sub.key] ?: throw makeException("Argument '${sub.key}' not found"))
        is SubRef.Index -> follow(sub, call.items[(sub.index + call.items.size) % call.items.size])
    }

    fun follow(following: SubRef?, reference: Reference): SimpleInterpreter {
        return when (reference) {
            is Reference.RLabel -> {
                val label = generateSequence(this) { it.parent }.find { it.call.label == reference.label } ?: throw makeException("Label ${reference.label} not found")
                SimpleInterpreter.resolve(label, reference.children)
            }
            is Reference.RArgument -> {
                SimpleInterpreter.resolve(arguments!!, reference.children)
            }
            is Reference.RStatic -> {
                SimpleInterpreter(root.calls[reference.key] ?: throw makeException("Static call '${reference.key} not found."), null, SubRef.Key(reference.key), language, null, root)
            }
            is Reference.RCall -> SimpleInterpreter(reference.call, this, following)
            is Reference.RVirtualCall -> SimpleInterpreter(reference.getCall(this), this, following)
        }
    }

    override fun execute(reference: Reference): Any? = follow(null, reference).execute()
    fun execute(): Any? {
        return if (language == Languages.INTERPRET) {
            val interpretation = function.interpretation
            if (interpretation != null) interpretation.invoke(SimpleInterpreter(
                    call = this.call,
                    parent = this,
                    subRef = SubRef.Key("language_" + Languages.INTERPRET),
                    language = this.language,
                    arguments = this,
                    root = this.root
            ))
            else execution(Languages.INTERPRET).execute()
        } else
            execution(language).execute()
    }

    fun execution(language: String): SimpleInterpreter {
        var executionName = language
        val execution = function.executions[language] ?:
                function.executions[Languages.DEFAULT].also { executionName = Languages.DEFAULT } ?:
                throw makeException("Execution '$language' not found")
        return SimpleInterpreter(
                execution,
                null,
                SubRef.Key("language_" + executionName),
                language,
                this,
                this.root
        )
    }

    override fun literal(): Any? = call.literal

    override fun identify(reference: Reference): Int = follow(null, reference).identify()
    fun identify(): Int = call.hashCode()

    fun path(): List<SubRef> = generateSequence(this) { it.parent }
            .map { it.subRef }
            .takeWhile { it != null }
            .map { it!! }
            .toList()
            .plus(SubRef.Key(arguments?.call?.function ?: "STATIC"))
            .asReversed()

    fun stackTrace(): List<List<SubRef>> = generateSequence(this) { it.arguments }
            .map { it.path() }
            .toList()

    fun makeException(message: String) = Exception("$message at ${stackTrace().joinToString("\n") { it.joinToString(".") }}")

    companion object {
        fun resolve(start: SimpleInterpreter, subRefs: List<SubRef>): SimpleInterpreter {
            var current = start
            for (subRef in subRefs) {
                current = current.follow(subRef)
            }
            return current
        }
    }
}