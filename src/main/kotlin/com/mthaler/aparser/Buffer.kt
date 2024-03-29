package com.mthaler.aparser

data class Buffer(val t: String?) {

    fun isEmpty():Boolean = text().isEmpty()

    fun length(): Int = text().length

    fun startsWith(prefix: String): Boolean = text().startsWith(prefix)

    fun startsWith(char: Char): Boolean = text().startsWith(char)

    fun substring(startIndex: Int): String = text().substring(startIndex)

    operator fun get(index: Int): Char = text()[index]

    fun text(): String {
        return if (t != null) {
            return t
        } else {
            ""
        }
    }
}

fun String.toBuffer(): Buffer = Buffer(this)
