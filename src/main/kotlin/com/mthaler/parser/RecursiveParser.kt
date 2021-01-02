package com.mthaler.parser

abstract class RecursiveParser<T>: Parser<T> {

    protected lateinit var parser: Parser<T>

    override fun parse(input: String): Result<T> = parser.parse(input)
}