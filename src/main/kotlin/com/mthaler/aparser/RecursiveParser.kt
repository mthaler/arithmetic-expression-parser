package com.mthaler.aparser

import com.mthaler.aparser.util.Result

open class RecursiveParser<T>: Parser<T> {

    var parser: Parser<T>? = null

    override fun parse(input: String): Result<T> =
        parser?.parse(input) ?: throw Exception("Parser not set")
}
