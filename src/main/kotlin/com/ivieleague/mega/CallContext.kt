package com.ivieleague.mega

/**
 *
 * \n * Created by josep on 2/9/2017.
 */
//
//interface CallContext {
//    val root: Call
//    val context: Call
//    val labels: MutableMap<String, Call>
//
//    fun copy():CallContext
//    fun mutateContext(context: Call):CallContext
//}
//
//class NormalCallContext(
//        override var root: Call,
//        override var context: Call,
//        override var labels: MutableMap<String, Call> = HashMap()
//) : CallContext{
//
//    override fun copy(): CallContext = NormalCallContext(root, context, labels)
//
//    override fun mutateContext(context: Call): CallContext{
//        this.context = context
//        return this
//    }
//}