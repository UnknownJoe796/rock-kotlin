package com.ivieleague.mega

class SimpleInterpreter(
        val call: Call,
        val parent: SimpleInterpreter?,
        val subRef: SubRef? = null,
        val language: String = call.language ?: parent?.language ?: Languages.INTERPRET,
        var arguments: SimpleInterpreter? = parent?.arguments,
        val root: Root = parent!!.root
) : InterpretationInterface {

    val function: Function = root.functions[call.function] ?: throw makeException("Function '${call.function}' not found")

    override fun resolve(subRef: SubRef): SimpleInterpreter = when (subRef) {
    //TODO: If resolving from defaults, make this temporarily the "arguments"
        is SubRef.Key -> resolve(subRef, call.arguments[subRef.key] ?: function.arguments[subRef.key] ?: throw makeException("Argument '${subRef.key}' not found"))
        is SubRef.Index -> resolve(subRef, call.items[(subRef.index + call.items.size) % call.items.size])
    }

    override fun quickResolveKey(key: String): InterpretationInterface {
        val subRef = SubRef.Key(key)
        return resolve(subRef, call.arguments[subRef.key] ?: function.arguments[subRef.key] ?: throw makeException("Argument '${subRef.key}' not found"))
    }

    override fun quickResolveIndex(index: Int): InterpretationInterface {
        val subRef = SubRef.Index(index)
        return resolve(subRef, call.items[(subRef.index + call.items.size) % call.items.size])
    }

    fun resolve(following: SubRef?, reference: Reference): SimpleInterpreter {
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

    override fun call(): Call = call

    override fun execute(): Any? {
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
                current = current.resolve(subRef)
            }
            return current
        }
    }
}