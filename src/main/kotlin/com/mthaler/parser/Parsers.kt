package com.mthaler.parser

typealias Parser<T> = (String) -> Result<T>

fun charLiteral(c: Char): Parser<Char> = { input ->
    if (input.startsWith(c))
        Result.OK(c, input.substring(1))
    else
        Result.Err("a '$c'", input)
}

fun stringLiteral(s: String): Parser<String> = { input ->
    if (input.startsWith(s))
        Result.OK(s, input.substring(s.length))
    else
        Result.Err("'$s'", input)
}