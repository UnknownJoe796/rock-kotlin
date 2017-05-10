package com.ivieleague.generic

import java.io.Writer

/**
 * Created by josep on 5/10/2017.
 */
class TabWriter(val out: Writer) : Writer() {

    var tabs = 0

    override fun write(cbuf: CharArray?, off: Int, len: Int) = out.write(cbuf, off, len)
    override fun flush() = out.flush()
    override fun close() = out.close()

    fun writeln(relativeIndentation: Int = 0) {
        tabs += relativeIndentation
        write("\n")
        repeat(tabs) {
            write("\t")
        }
    }

    inline fun <T> writeSeparatingByLine(iterable: Iterable<T>, action: TabWriter.(T) -> Unit) {
        val iter = iterable.iterator()
        while (iter.hasNext()) {
            action(iter.next())
            if (iter.hasNext()) writeln()
        }
    }
}