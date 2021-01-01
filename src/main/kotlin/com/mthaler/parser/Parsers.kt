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

fun whitespace(input: String): Result<Unit> {
    if (input.isEmpty()) {
        return Result.Err("empty input", input)
    } else if (!input[0].isWhitespace()) {
        return Result.Err("not a whitespace", input)
    } else {
        for (i in 1 until input.length) {
            if (!input[i].isWhitespace()) {
                return Result.OK(Unit, input.substring(i))
            }
        }
        return Result.OK(Unit, input)
    }
}

fun digits(input: String): Result<String> {
    if (input.isEmpty()) {
        return Result.Err("empty input", input)
    } else if (!input[0].isDigit()) {
        return Result.Err("not a digit", input)
    } else {
        val sb = StringBuffer()
        sb.append(input[0])
        for (i in 1 until input.length) {
            val c = input[i]
            if (c.isDigit())
                sb.append(c)
            else
                return Result.OK(sb.toString(), input.substring(i))
        }
        return Result.OK(sb.toString(), "")
    }
}