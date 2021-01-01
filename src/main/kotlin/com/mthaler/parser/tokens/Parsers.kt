package com.mthaler.parser.tokens

import com.mthaler.parser.Result

typealias TokenParser = (String) -> Result<String>

val numberRegex = Regex("^\\d+(\\.\\d*)?([eE][+-]?\\d+)?")

fun charLiteral(c: Char): TokenParser = { input ->
    if (input.startsWith(c))
        Result.OK(c.toString(), input.substring(1))
    else
        Result.Err("a '$c'", input)
}

fun stringLiteral(s: String): TokenParser = { input ->
    if (input.startsWith(s))
        Result.OK(s, input.substring(s.length))
    else
        Result.Err("'$s'", input)
}

fun whitespaces(input: String): Result<String> {
    if (input.isEmpty()) {
        return Result.Err("whitespaces", input)
    } else if (!input[0].isWhitespace()) {
        return Result.Err("whitespaces", input)
    } else {
        val sb = StringBuffer()
        sb.append(input[0])
        for (i in 1 until input.length) {
            val c = input[i]
            if (c.isWhitespace())
                sb.append(c)
            else
                return Result.OK(sb.toString(), input.substring(i))
        }
        return Result.OK(sb.toString(), "")
    }
}


fun digits(input: String): Result<String> {
    if (input.isEmpty()) {
        return Result.Err("digits", input)
    } else if (!input[0].isDigit()) {
        return Result.Err("digits", input)
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

fun number(input: String): Result<String> {
    if (input.isEmpty()) {
        return Result.Err("number", input)
    } else {
        val m = numberRegex.find(input)
        if (m != null) {
            return Result.OK(m.value, input.substring(m.range.endInclusive + 1))
        } else {
            return Result.Err("number", input)
        }
    }
}
