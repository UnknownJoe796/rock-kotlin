package com.ivieleague.generic

/**
 *
 * Created by josep on 2/25/2017.
 */

fun <E> MutableList<E>.truncate(endSize: Int) {
    var skip = endSize
    val iter = iterator()
    while (iter.hasNext()) {
        iter.next()
        if (skip > 0) {
            skip--
        } else {
            iter.remove()
        }
    }
    //[0, 1, 2, 3, 4] truncate 3
    //
}