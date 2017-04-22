package com.ivieleague.mega

sealed class Reference {
    class RLabel(val label: String, val children: List<SubRef>) : Reference()
    class RArgument(val children: List<SubRef>) : Reference()
    class RCall(val call: Call) : Reference()
    class RVirtualCall(val getCall: (InterpretationInterface) -> Call) : Reference()
}