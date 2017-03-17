package com.ivieleague.mega

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * \n * Created by josep on 2/15/2017.
 */
class CoreTest {
    @Test
    fun basicTest() = assertEquals(
            3,
            DSL {
                "literal" - interpretation { it.context.literal }
                "plus" - interpretation {
                    val context = it.mutateContext()
                    context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
                }
                "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                    "left" - literal(root("literal"), 1)
                    "right" - literal(root("literal"), 2)
                }
            }.let(::TrackingCallRealization).getAndRun<Int>("main")
    )

    @Test
    fun indirectTest() = assertEquals(
            3,
            DSL {
                "literal" - interpretation { it.context.literal }
                "plus" - interpretation {
                    val context = it.mutateContext()
                    context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
                }
                "indirect plus" - abstract(root("plus")) {}
                "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("indirect plus")) {
                    "left" - literal(root("literal"), 1)
                    "right" - literal(root("literal"), 2)
                }
            }.let(::TrackingCallRealization).getAndRun<Int>("main")
    )

    @Test
    fun labelInheritTest(){
        DSL {
            "a" - abstract {
                label = "label"
                invocation = { "Success" }
                "b" - abstract {
                    "c" - label("label")
                }
            }
            "/interpret" - call(Call.LANGUAGE_INTERPRET, root("a")){}
            language = "/interpret"
        }.invokeAsRoot()
    }

    @Test
    fun labelTest(){
        DSL {
            "a" - call("/interpret", null) {
                label = "label"
                invocation = { "Success" }
                "b" - abstract {
                    "c" - label("label")
                }
            }
            "/interpret" - root("a")
            language = "/interpret"
        }.invokeAsRoot()
    }

    @Test
    fun languageTest() {
        val result = DSL {
            "literal" - abstract {
                "_lang_test" - action { "Literal: " + it.context.literal.toString() }
                invocation = { it.context.literal }
            }
            "plus" - abstract {
                "_lang_test" - action {
                    val context = it.mutateContext()
                    "(" + context.getAndRun("left") + ") + (" + context.getAndRun("right") + ")"
                }
                invocation = {
                    val context = it.mutateContext()
                    context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
                }
            }
            "main" - call("_lang_test", root("plus")) {
                "left" - literal(root("literal"), 1)
                "right" - literal(root("literal"), 2)
            }
        }.let(::TrackingCallRealization).getAndRun<String>("main")
        println(result)
        assertEquals("(Literal: 1) + (Literal: 2)", result)
    }
}