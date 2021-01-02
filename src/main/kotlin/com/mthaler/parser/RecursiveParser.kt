package com.mthaler.parser

class RecursiveParser<T>: Parser<T> {

    lateinit var parser: Parser<T>

    override fun parse(input: String): Result<T> = parser.parse(input)
}