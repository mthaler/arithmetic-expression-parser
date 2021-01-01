package com.mthaler

typealias Parser<T> = (String) -> Result<T>

// combinators

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
    sequence(this, p2)

infix fun <T> Parser<T>.or(p2: Parser<T>): Parser<T> =
    orderedChoice(this, p2)