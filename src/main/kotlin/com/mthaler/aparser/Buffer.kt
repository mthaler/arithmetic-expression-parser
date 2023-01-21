package com.mthaler.aparser

class Buffer(val text: String) {

    fun isEmpty():Boolean = text.isEmpty()

    fun length(): Int = text.length
}

fun String.toBuffer(): Buffer = Buffer(this)