package com.ivieleague.mega

import org.junit.Test

/**
 * Tests the core functionality.
 * Created by josep on 4/22/2017.
 */
class BasicTest {

    fun InterpretationInterface.execute(key: String) = execute(Reference.RArgument(listOf(SubRef.Key(key))))

    @Test
    fun testBasic() {
        val root = StandardRoot().apply {
            functions["add"] = StandardFunction {
                (it.execute("left") as Int) + (it.execute("right") as Int)
            }
            functions["literal"] = StandardFunction(InterpretationInterface::literal)
            functions["increment"] = StandardFunction().apply {
                executions[Languages.DEFAULT] = StandardCall("add").apply {
                    arguments["left"] = Reference.RArgument(listOf(SubRef.Key("value")))
                    arguments["right"] = Reference.RCall(StandardCall("literal", literal = 1))
                }
            }

            calls["main"] = StandardCall("increment", language = Languages.INTERPRET).apply {
                arguments["value"] = Reference.RCall(StandardCall("literal", literal = 3))
            }
        }

        val interpreter = SimpleInterpreter(root.calls["main"]!!, subRef = SubRef.Key("main"), language = Languages.INTERPRET, root = root, parent = null)
        println(interpreter.execute())
    }
}