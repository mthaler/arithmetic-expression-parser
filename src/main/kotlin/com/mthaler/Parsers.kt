package com.mthaler

typealias Parser<T> = (String) -> Result<T>

fun dot(input: String): Result<Unit> =
    if (input.startsWith("."))
        Result.OK(Unit, input.substring(1))
    else
        Result.Err("a dot", input)

fun integer(input: String): Result<Int> {
    if (input.isEmpty() || !input[0].isDigit()) {
        return Result.Err("an integer", input)
    } else {
        var value = 0
        for (i in input.indices) {
            if (input[i].isDigit()) {
                value = (value * 10) + (input[i] - '0')
            } else {
                return Result.OK(value, input.substring(i))
            }
        }
        return Result.OK(value, "")
    }
}

fun string(s: String): Parser<Unit > = { input ->
    if (input.startsWith(s))
        Result.OK(Unit, input.substring(s.length))
    else
        Result.Err("'$s'", input)
}

fun quotedString(input: String): Result<String> {
    if (input.isEmpty() || input[0] != '"') {
        return Result.Err("a quoted string", input)
    }

    var escaped = false
    var string = ""
    for (i in 1 until input.length) {
        val c = input[i]
        when {
            c == '"' && !escaped ->
                return Result.OK(string, input.substring(i + 1))
            c == '\\' && !escaped ->
                escaped = true
            else -> {
                if (escaped) string += '\\'
                escaped = false
                string += c
            }
        }
    }

    return Result.Err("a terminated quoted string", input)
}

fun whitespace(input: String): Result<Unit> {
    for (i in input.indices) {
        if (!input[i].isWhitespace()) {
            return Result.OK(Unit, input.substring(i))
        }
    }
    return Result.OK(Unit, input)
}

fun <T1, T2> seq(p1: Parser<T1>, p2: Parser<T2>): Parser<Pair<T1, T2>> = { input ->
    p1(input).flatMap { r1, rest -> p2(rest).map { r2 -> Pair(r1, r2) } }
}

fun <T> choice(p1: Parser<T>, p2: Parser<T>): Parser<T> = { input ->
    when(val r1 = p1(input)) {
        is Result.OK -> r1
        is Result.Err -> p2(input).mapExpected { e ->  "${r1.expected} or $e" }
    }
}

fun <T, U> Parser<T>.map(f: (T) -> U): Parser<U> = { input ->
    this(input).map(f)
}

fun <T, U> Parser<T>.means(u: U): Parser<U> = map { u }

infix fun <T1, T2> Parser<T1>.then(p2: Parser<T2>): Parser<Pair<T1, T2>> =
    seq(this, p2)

infix fun <T> Parser<T>.or(p2: Parser<T>): Parser<T> =
    choice(this, p2)

fun <X, T> Parser<X>.before(p: Parser<T>): Parser<T> =
    seq(this, p).map { it.second }

fun <T, Y> Parser<T>.followedBy(y: Parser<Y>): Parser<T> =
    seq(this, y).map { it.first }

fun <X, T, Y> Parser<T>.between(x: Parser<X>, y: Parser<Y>): Parser<T> =
    x.before(this).followedBy(y)