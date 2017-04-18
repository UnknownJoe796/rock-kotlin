package com.ivieleague.mega

/**
 * Alternative system.
 * Created by josep on 4/12/2017.
 */

interface Root {
    fun function(key: String): Function
    fun call(key: String): Call
}

interface Function {
    val interpretation: (InterpretationInterface) -> Any?
    fun execution(key: String): Reference
    fun argument(key: String): Reference
}

interface Call {
    val language: String?
    val function: String
    val label: String
    val literal: Any?
    fun argument(key: String): Reference
    fun item(index: Int): Reference
}

sealed class Reference {
    class Label(val label: String) : Reference()
    class Definition(val call: Call) : Reference()
    class Argument(val key: String) : Reference()
}

interface InterpretationInterface {
    fun execute(key: String)
    fun identify(key: String)
    /*
    What do I really need?
    - Execute an argument
    - Identify an argument (for breaking loops)
    - Stack trace?
    */
}

//interface Root {
//    val calls:Map<String, Call>
//    val functions:Map<String, Function>
//}
//
//interface Function {
//    val defaults: Map<String, Reference>
//}
//
//interface Call {
//    val arguments: Map<String, Reference>
//}
//
//interface MachineState {
//    fun argument(key: String): Call
//    fun lambdaArgument(key:String):Call
//}
//
//sealed class Reference {
//
//    class Label() : Reference()
//    class Definition() : Reference()
//    class Argument() : Reference()
//    class LambdaArgument() : Reference()
//}