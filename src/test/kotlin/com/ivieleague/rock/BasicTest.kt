package com.ivieleague.rock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.ivieleague.generic.PushbackReaderDebug
import com.ivieleague.generic.TabWriter
import com.ivieleague.rock.builder.execute
import org.junit.Test
import java.io.StringWriter

/**
 * Tests the core functionality.
 * Created by josep on 4/22/2017.
 */
class BasicTest {


    @Test
    fun testBasic() {
        val root = StandardRoot().apply {
            functions["base"] = StandardFunction(null)
            functions["add"] = StandardFunction {
                (it.execute("left") as Int) + (it.execute("right") as Int)
            }
            functions["rock.integer.signed.4.literal"] = StandardFunction { it.call().literal }
            functions["increment"] = StandardFunction().apply {
                executions[Languages.DEFAULT] = Reference.RCall(StandardCall("add").apply {
                    arguments["left"] = Reference.RArgument(listOf(SubRef.Key("value")))
                    arguments["right"] = Reference.RCall(StandardCall("rock.integer.signed.4.literal", literal = 1))
                })
            }

            calls["main"] = StandardCall("increment", language = Languages.INTERPRET).apply {
                arguments["value"] = Reference.RCall(StandardCall("rock.integer.signed.4.literal", literal = 3))
            }
        }

        val yaml = ObjectMapper(YAMLFactory()).writeValueAsString(PrimitiveConversion.run { root.toPrimitive() })
        println(yaml)
        val recreated = PrimitiveConversion.run { ObjectMapper(YAMLFactory()).readValue(yaml, Map::class.java).toRoot() }
        val recreatedYaml = ObjectMapper(YAMLFactory()).writeValueAsString(PrimitiveConversion.run { recreated.toPrimitive() })
        println(recreatedYaml)

        val manual = root.toManual()
        println(manual)
        val alternate = manual.toRootManual()

        val interpreter = SimpleInterpreter(root.calls["main"]!!, subRef = SubRef.Key("main"), language = Languages.INTERPRET, root = root, parent = null)
        println(interpreter.execute())
    }

    fun Root.toManual(): String {
        val writer = StringWriter()
        ManualRepresentation().run {
            TabWriter(writer).apply {
                writeFile(this@toManual)
            }
        }
        return writer.toString()
    }

    fun String.toRootManual(): Root = ManualRepresentation().run {
        val manualReader = PushbackReaderDebug(this@toRootManual.reader(), 128)
        try {
            manualReader.parseFile()
        } catch(e: Throwable) {
            throw IllegalStateException("At ${manualReader.position}", e)
        }
    }
}