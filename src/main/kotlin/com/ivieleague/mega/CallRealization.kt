package com.ivieleague.mega


/**
 * \n * Created by josep on 2/13/2017.
 */
interface CallRealization {
    val call: Call
    val root: Call

    fun copy(): CallRealization

    fun mutateRoot(): CallRealization
    fun mutateLabel(label: String): CallRealization
    fun mutateContext(): CallRealization
    fun mutateChild(subref: SubRef, call: Call): CallRealization
    fun mutateThroughPrototypesIterator(): Iterator<CallRealization>

    fun mutateInvoke(): Any?

    fun mutateExists(key: String): Boolean
            = mutateThroughPrototypesIterator().asSequence().mapNotNull { call.children[key] }.any()

    fun exists(index: Int): Boolean
            = index in call.list.indices

    fun mutateKey(key: String): CallRealization
            = mutateThroughPrototypesIterator().asSequence().mapNotNull { call.children[key] }.firstOrNull()?.traverse(this) ?: throw RealizationException(this, "Key $key not found.")

    fun mutateIndex(index: Int): CallRealization = call.list[index].traverse(this)

    /////
    fun invoke(): Any? = copy().mutateInvoke()

    fun debugInfo(): String {
        return "Call to ${call.prototype?.let { JSON.fromRef(it) }} in context "
    }

    fun mutateGetKeys(): Sequence<String> = mutateThroughPrototypesIterator().asSequence().flatMap {
        it.call.children.keys.asSequence()
    }.distinct()
}

fun CallRealization.mutateLiteral() = mutateThroughPrototypesIterator().asSequence().mapNotNull { it.call.literal }.firstOrNull()
fun CallRealization.getKeys() = copy().mutateGetKeys()
fun CallRealization.realizationMap(): Sequence<Pair<String, CallRealization>> = getKeys().map { it to get(it) }
fun CallRealization.realizationList(): Sequence<CallRealization> = call.list.asSequence().map { it.traverse(copy()) }
operator fun CallRealization.get(key: String): CallRealization = copy().mutateKey(key)
operator fun CallRealization.get(index: Int): CallRealization = copy().mutateIndex(index)
inline fun <reified T> CallRealization.getAndRun(key: String): T = copy().mutateKey(key).mutateInvoke() as T
inline fun <reified T> CallRealization.getAndRun(index: Int): T = copy().mutateIndex(index).mutateInvoke() as T
inline fun <reified T> CallRealization.invokeList(): Sequence<T> = call.list.asSequence().map { it.traverse(copy()).mutateInvoke() as T }