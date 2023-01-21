package com.mthaler.aparser

data class Buffer(val text: String) {

    fun isEmpty():Boolean = text.isEmpty()

    fun length(): Int = text.length

    fun startsWith(prefix: String) = text.startsWith(prefix)

    fun startsWith(char: Char) = text.startsWith(char)
}

fun String.toBuffer(): Buffer = Buffer(this)