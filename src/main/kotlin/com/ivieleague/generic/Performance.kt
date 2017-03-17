package com.ivieleague.generic

/**
 * Some inline functions for measuring the performance of code.
 *
 * I am not responsible for your use / misuse of this.
 *
 * Feel free to contribute!
 *
 * Created by josep on 2/25/2017.
 */
object Performance {
    inline fun nanoseconds(
            iterations: Int = 1000,
            action: () -> Unit
    ): Double {
        //hack around kotlin bug
        val startBoxed: Long? = System.nanoTime()
        val start: Long = startBoxed!!
        for (i in 1..iterations) {
            action()
        }
        return (System.nanoTime() - start) / iterations.toDouble()
    }

    inline fun milliseconds(
            iterations: Int = 1000,
            action: () -> Unit
    ): Double = nanoseconds(iterations, action) / 1000000.0

    inline fun seconds(
            iterations: Int = 1000,
            action: () -> Unit
    ): Double = milliseconds(iterations, action) / 1000.0
}