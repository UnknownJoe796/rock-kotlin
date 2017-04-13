package com.ivieleague.mega

/**
 * \n * Created by josep on 2/22/2017.
 */
class LazyMergedCall(val calls: List<Call>) : Call {
    override val prototype: Ref? get() = calls.asSequence().mapNotNull(Call::prototype).firstOrNull()
    override val children: Map<String, Ref> = object : Map<String, Ref> {
        override val entries: Set<Map.Entry<String, Ref>>
            get() = keys.mapTo(HashSet()) {
                object : Map.Entry<String, Ref> {
                    override val key: String
                        get() = it
                    override val value: Ref
                        get() = get(it)!!
                }
            }
        override val keys: Set<String>
            get() = calls.flatMap { it.children.keys }.toSet()
        override val size: Int
            get() = calls.flatMap { it.children.keys }.distinct().count()
        override val values: Collection<Ref>
            get() = calls.flatMap { it.children.values }.toList()

        override fun containsKey(key: String): Boolean = calls.any { it.children.containsKey(key) }
        override fun containsValue(value: Ref): Boolean = calls.any { it.children.containsValue(value) }
        override fun get(key: String): Ref? {
            val options = calls.asSequence().mapNotNull { it.children[key] }
            if (options.firstOrNull() !is Ref.Definition) {
                return options.firstOrNull()
            }
            return Ref.Definition(SubRef.Key(key), LazyMergedCall(
                    options.mapNotNull { (it as? Ref.Definition)?.call }.toList()
            ))
        }

        override fun isEmpty(): Boolean = calls.all { it.children.isEmpty() }
    }
    override val list: List<Ref> = calls.first().list
    override val label: String? = calls.asSequence().mapNotNull(Call::label).firstOrNull()
    override val language: String? = calls.asSequence().mapNotNull(Call::language).firstOrNull()
    override val literal: Any? = calls.asSequence().mapNotNull(Call::literal).firstOrNull()
    override val invocation: ((CallRealization) -> Any?)? = calls.asSequence().mapNotNull(Call::invocation).firstOrNull()

    override fun equals(other: Any?): Boolean = other is LazyMergedCall && this.calls equalsList other.calls
    override fun hashCode(): Int = this.calls.sumBy(Call::hashCode)
}