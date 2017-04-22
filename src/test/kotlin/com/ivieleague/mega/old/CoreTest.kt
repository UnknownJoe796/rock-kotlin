package com.ivieleague.mega.old

import com.ivieleague.mega.old.*
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
                "literal" - interpretation { it.mutateContext().call.literal }
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
    fun contextTest() = assertEquals(
            3,
            DSL {
                "literal" - interpretation { it.mutateContext().call.literal }
                "plus" - interpretation {
                    val context = it.mutateContext()
                    context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
                }
                "plusTwo" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                    "left" - context("start")
                    "right" - literal(root("literal"), 2)
                }
                "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plusTwo")) {
                    "start" - literal(root("literal"), 1)
                }
            }.let(::TrackingCallRealization).getAndRun<Int>("main")
    )

    @Test
    fun sameTest() {
        val litInterp = { it: CallRealization -> it.mutateContext().call.literal }
        val plusInterp = { it: CallRealization ->
            val context = it.mutateContext()
            context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
        }
        assertEquals(true, DSL {
            "literal" - interpretation(litInterp)
            "plus" - interpretation(plusInterp)
            "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                "left" - literal(root("literal"), 1)
                "right" - literal(root("literal"), 2)
            }
        } same DSL {
            "literal" - interpretation(litInterp)
            "plus" - interpretation(plusInterp)
            "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                "left" - literal(root("literal"), 1)
                "right" - literal(root("literal"), 2)
            }
        })
    }

    @Test
    fun differentTest() {
        assertEquals(false, DSL {
            "literal" - interpretation { it.mutateContext().call.literal }
            "plus" - interpretation {
                val context = it.mutateContext()
                context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
            }
            "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                "left" - literal(root("literal"), 1)
                "right" - literal(root("literal"), 2)
            }
        } same DSL {
            "literal" - interpretation { it.mutateContext().call.literal }
            "plus" - interpretation {
                val context = it.mutateContext()
                context.getAndRun<Int>("left") + context.getAndRun<Int>("right")
            }
            "main" - call(Call.Companion.LANGUAGE_INTERPRET, root("plus")) {
                "left" - literal(root("literal"), 4)
                "right" - literal(root("literal"), 2)
            }
        })
    }

    @Test
    fun indirectTest() = assertEquals(
            3,
            DSL {
                "literal" - interpretation { it.mutateContext().call.literal }
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
    fun inheritanceTest() {
        DSL {
            "super" - call("/interpret", null) {
                "child" - action { println("Success") }
            }
            "sub" - call("/language", root("super")) {}
            "/interpret" - root("sub", "child")
            language = "/interpret"
        }.invokeAsRoot()
    }

    @Test
    fun languageTest() {
        val result = DSL {
            "literal" - abstract {
                "_lang_test" - action { "Literal: " + it.mutateContext().call.literal.toString() }
                invocation = { it.mutateContext().call.literal }
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