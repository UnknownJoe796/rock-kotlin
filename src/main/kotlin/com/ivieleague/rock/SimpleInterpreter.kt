package com.ivieleague.rock

class SimpleInterpreter(
        val call: Call,
        val parent: SimpleInterpreter?,
        val subRef: SubRef? = null,
        val language: String = call.language ?: parent?.language ?: Languages.INTERPRET,
        val arguments: SimpleInterpreter? = parent?.arguments,
        val root: Root = parent!!.root,
        val lambdaArguments: SimpleInterpreter? = null
) : InterpretationInterface {


    val function: Function = root.functions[call.function] ?: throw makeException("Function '${call.function}' not found")
    val asArguments by lazy {
        SimpleInterpreter(
                call = call,
                parent = parent,
                subRef = subRef,
                language = language,
                arguments = this,
                root = root,
                lambdaArguments = this.lambdaArguments
        )
    }

    override fun resolve(subRef: SubRef): SimpleInterpreter = when (subRef) {
        is SubRef.Key -> call.arguments[subRef.key]?.let { resolve(subRef, it) } ?: function.getArgument(root, subRef.key)?.let { asArguments.resolve(subRef, it) } ?: throw makeException("Key '${subRef.key}' not found")
        is SubRef.Index -> resolve(subRef, call.items[(subRef.index + call.items.size) % call.items.size])
    }

    override fun quickResolveKey(key: String): SimpleInterpreter {
        val subRef = SubRef.Key(key)
        return resolve(subRef, call.arguments[subRef.key] ?: function.getArgument(root, subRef.key) ?: throw makeException("Argument '${subRef.key}' not found"))
    }

    override fun quickResolveIndex(index: Int): SimpleInterpreter {
        val subRef = SubRef.Index(index)
        return resolve(subRef, call.items[(subRef.index + call.items.size) % call.items.size])
    }

    fun resolve(following: SubRef?, reference: Reference): SimpleInterpreter {
        return when (reference) {
            is Reference.RLabel -> {
                val label = generateSequence(this) { it.parent }.find { it.call.label == reference.label } ?:
                        throw makeException("Label ${reference.label} not found")
                SimpleInterpreter.resolve(label, reference.children)
            }
            is Reference.RArgument -> {
                SimpleInterpreter.resolve(arguments ?: throw makeException("Arguments not defined"), reference.children)
            }
            is Reference.RLambdaArgument -> {
                SimpleInterpreter.resolve(lambdaArguments
                        ?: throw makeException("Lambda Arguments not defined"), reference.children)
            }
            is Reference.RStatic -> {
                val static = SimpleInterpreter(root.calls[reference.key] ?:
                        throw makeException("Static call '${reference.key}' not found."), null, SubRef.Key(reference.key), language, null, root)
                SimpleInterpreter.resolve(static, reference.children)
            }
            is Reference.RCall -> SimpleInterpreter(reference.call, this, following)
            is Reference.RVirtualCall -> SimpleInterpreter(reference.getCall(this), this, following)
        }
    }

    override fun call(): Call = call

    override fun key(): String = when (subRef) {
        is SubRef.Key -> subRef.key
        is SubRef.Index -> subRef.index.toString()
        else -> throw IllegalStateException()
    }

    override fun execute(): Any? {
        return if (language == Languages.INTERPRET) {
            val interpretation = function.interpretation
            if (interpretation != null) interpretation.invoke(this)
            else resolveExecution(Languages.INTERPRET).execute()
        } else
            resolveExecution(language).execute()
    }

    fun resolveExecution(language: String): InterpretationInterface {
        var executionName = language
        val executionReference = function.getExecution(root, language) ?:
                function.getExecution(root, Languages.DEFAULT).also { executionName = Languages.DEFAULT } ?:
                throw makeException("Execution '$language' not found for function ${call.function}")
        return SimpleInterpreter(
                call = this.call,
                parent = null,
                subRef = this.subRef,
                language = this.call.language ?: language,
                arguments = this,
                root = this.root
        ).resolve(SubRef.Key("execution_" + executionName), executionReference)
    }

    override fun addedArguments(interpretationInterface: InterpretationInterface): InterpretationInterface = SimpleInterpreter(
            call = this.call,
            parent = this.parent,
            subRef = this.subRef,
            language = this.language,
            arguments = this.arguments,
            root = this.root,
            lambdaArguments = interpretationInterface as SimpleInterpreter
    )

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