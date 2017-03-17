package com.ivieleague.mega

import java.util.*

class SimpleCallRealization(
        override var call: Call,
        override var root: Call,
        override var context: Call,
        var labels: MutableMap<String, Call> = HashMap()
) : CallRealization {

    constructor(root: Call) : this(root, root, root)

    fun set(other: SimpleCallRealization) {
        this.call = other.call
        this.root = other.root
        this.context = other.context
        this.labels = other.labels
    }

    override fun copy(): CallRealization = SimpleCallRealization(call, root, context, labels)
    override fun mutateRoot(): CallRealization = this.apply { call = root }
    override fun mutateLabel(label: String): CallRealization = this.apply { call = labels[label]!! }
    override fun mutateContext(): CallRealization = this.apply { call = context }
    override fun mutateChild(subref: SubRef, call: Call): CallRealization = this.apply {
        this.call = call
        if (call.label != null)
            labels[call.label!!] = call
    }

    fun mutateSetContext(context: Call): CallRealization {
        this.context = context
        this.labels = HashMap()
        return this
    }

    override fun mutateThroughPrototypesIterator(): Iterator<CallRealization> = object : Iterator<CallRealization> {
        var nextCalled = 0
        override fun hasNext(): Boolean = nextCalled == 0 || call.prototype != null

        override fun next(): CallRealization {
            nextCalled++
            return if (nextCalled == 1) {
                this@SimpleCallRealization
            } else if (nextCalled == 2) {
                val currentCall = call
                (call.prototype!!.traverse(this@SimpleCallRealization) as SimpleCallRealization).mutateSetContext(currentCall)
            } else {
                call.prototype!!.traverse(this@SimpleCallRealization)
            }
        }
    }

    override fun mutateInvoke(): Any? {
        //Traverse through the executions until an interpretation is found
        while (true) {
            val language = this.call.language ?: throw IllegalArgumentException("Call cannot be abstract")
            //Traverse through prototypes until a ref is found
            var traversing = false
            for (thisRef in mutateThroughPrototypesIterator()) {
                if (language == Call.LANGUAGE_INTERPRET && thisRef.call.invocation != null)
                    return thisRef.call.invocation!!.invoke(thisRef)
                val ref = thisRef.call.children[language] ?: thisRef.call.children[Call.LANGUAGE_DEFAULT]
                if (ref != null) {
                    ref.traverse(thisRef)
                    traversing = true
                    break
                }
            }
            if (!traversing) throw IllegalStateException("No interpretation found")
        }
    }
}