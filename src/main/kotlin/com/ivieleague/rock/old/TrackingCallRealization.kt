package com.ivieleague.rock.old

import com.ivieleague.generic.truncate
import java.util.*

class TrackingCallRealization(
        override var call: Call,
        override var root: Call,
        var context: Call,
        var contextSize: Int = 0,
        var labels: MutableMap<String, Pair<Call, Int>> = HashMap(),
        var refs: MutableList<SubRef> = ArrayList(),
        var contextRefs:List<SubRef> = ArrayList()
) : CallRealization {

    constructor(root: Call) : this(root, root, root)

    fun set(other: TrackingCallRealization) {
        this.call = other.call
        this.root = other.root
        this.context = other.context
        this.labels = other.labels
        this.refs = other.refs
    }

    override fun copy(): CallRealization = TrackingCallRealization(call, root, context, contextSize, labels, ArrayList(refs), contextRefs)
    override fun mutateRoot(): CallRealization = this.apply { call = root; refs.clear() }
    override fun mutateLabel(label: String): CallRealization = this.apply {
        val (labelCall, labelSize) = labels[label]!!
        call = labelCall
        refs.truncate(labelSize)
    }

    override fun mutateContext(): CallRealization = this.apply {
        if (call == context) println("RECONTEXT")
        call = context
        refs.clear()
        refs.addAll(contextRefs)
    }
    override fun mutateChild(subref: SubRef, call: Call): CallRealization = this.apply {
        this.call = call
        this.refs.add(subref)
        if (call.label != null)
            labels[call.label!!] = call to refs.size
    }

    override fun mutateThroughPrototypesIterator(): Iterator<CallRealization> = object : Iterator<CallRealization> {
        var nextCalled = 0
        override fun hasNext(): Boolean = nextCalled == 0 || call.prototype != null

        override fun next(): CallRealization {
            nextCalled++
            return if (nextCalled == 1) {
                this@TrackingCallRealization
            } else if (nextCalled == 2) {
                val currentCall = call
                val currentRefs = refs.toList()
                val traversed = call.prototype!!.traverse(this@TrackingCallRealization) as TrackingCallRealization
                traversed.context = currentCall
                traversed.contextRefs = currentRefs
                traversed
            } else {
                call.prototype!!.traverse(this@TrackingCallRealization)
            }
        }
    }

    override fun mutateInvoke(): Any? {
        println("Invoke ${debugInfo()}")
        //Traverse through the executions until an interpretation is found
        while (true) {
            val language = this.call.language ?: Call.LANGUAGE_INTERPRET//throw IllegalArgumentException("Call cannot be abstract - ${debugInfo()}")
            //Traverse through prototypes until a ref is found
            var traversing = false
            for (thisRef in mutateThroughPrototypesIterator()) {
                println("Proto ${debugInfo()}")
                if (language == Call.LANGUAGE_INTERPRET && thisRef.call.invocation != null) {
                    println("Invoking ${debugInfo()}")
                    return thisRef.call.invocation!!.invoke(thisRef)
                }
                val ref = thisRef.call.children[language] ?: thisRef.call.children[Call.LANGUAGE_DEFAULT]
                if (ref != null) {
                    ref.traverse(thisRef)
                    println("Traversed ${debugInfo()}")
                    traversing = true
                    break
                }
            }
            if (!traversing) throw IllegalStateException("No interpretation found - ${debugInfo()}")
        }
    }

    override fun debugInfo(): String = "Path: ${refs.joinToString(".")} Context: ${(copy() as TrackingCallRealization).refs.joinToString(".")}"
}