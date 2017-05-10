package com.ivieleague.generic

import java.io.PushbackReader
import java.io.Reader
import java.nio.CharBuffer

/**
 * Created by josep on 5/10/2017.
 */
class PushbackReaderDebug(input: Reader, size: Int) : PushbackReader(input, size) {

    var position = 0L

    override fun unread(c: Int) {
        super.unread(c)
        position--
    }

    override fun unread(cbuf: CharArray, off: Int, len: Int) {
        super.unread(cbuf, off, len)
        position -= len
    }

    override fun unread(cbuf: CharArray) {
        super.unread(cbuf)
        position -= cbuf.size
    }

    override fun skip(n: Long): Long {
        position += n
        return super.skip(n)
    }

    override fun read(): Int {
        position += 1
        return super.read()
    }

    override fun read(cbuf: CharArray?, off: Int, len: Int): Int {
        position += len
        return super.read(cbuf, off, len)
    }

    override fun read(target: CharBuffer): Int {
        position += target.length
        return super.read(target)
    }

    override fun read(cbuf: CharArray): Int {
        position += cbuf.size
        return super.read(cbuf)
    }
}