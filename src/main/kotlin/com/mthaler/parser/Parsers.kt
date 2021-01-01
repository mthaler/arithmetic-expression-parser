package com.mthaler.parser

typealias Parser<T> = (String) -> Result<T>

val numberRegex = Regex("^\\d+(\\.\\d*)?([eE][+-]?\\d+)?")

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
        return Result.Err("whitespaces", input)
    } else if (!input[0].isWhitespace()) {
        return Result.Err("whitespaces", input)
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

fun <T1, T2> sequence(p1: Parser<T1>, p2: Parser<T2>): Parser<Pair<T1, T2>> = { input ->
    p1(input).flatMap { r1, rest -> p2(rest).map { r2 -> Pair(r1, r2) } }
}

fun <T> orderedChoice(p1: Parser<T>, p2: Parser<T>): Parser<T> = { input ->
    when(val r1 = p1(input)) {
        is Result.OK -> r1
        is Result.Err -> p2(input).mapExpected { e ->  "${r1.expected} or $e" }
    }
}

fun <T> optional(p: Parser<T>): Parser<T?> = { input ->
    when(val r = p(input)) {
        is Result.OK -> r
        is Result.Err -> Result.OK(null, input)
    }
}

fun <T, U> Parser<T>.map(f: (T) -> U): Parser<U> = { input ->
    this(input).map(f)
}

fun <T, U> Parser<T>.means(u: U): Parser<U> = map { u }

infix fun <T1, T2> Parser<T1>.and(p2: Parser<T2>): Parser<Pair<T1, T2>> =
    com.mthaler.seq(this, p2)

infix fun <T> Parser<T>.or(p2: Parser<T>): Parser<T> =
    orderedChoice(this, p2)