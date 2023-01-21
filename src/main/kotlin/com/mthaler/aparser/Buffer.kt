package com.mthaler.aparser

data class Buffer<T>(val t: T?) {

    fun isEmpty():Boolean = text().isEmpty()

    fun length(): Int = text().length

    fun startsWith(prefix: String): Boolean = text().startsWith(prefix)

    fun startsWith(char: Char): Boolean = text().startsWith(char)
    fun text(): String {
        return if (t != null && t is String) {
            return t
        } else {
            ""
        }
    }
}

fun String.toBuffer(): Buffer<String?> = Buffer(this)
