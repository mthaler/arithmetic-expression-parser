package com.mthaler.aparser

open class RecursiveParser<T>: Parser<T> {

    var parser: Parser<T>? = null

    override fun parse(input: String): Result<T> =
        parser?.parse(input) ?: throw Exception("Parser not set")
}