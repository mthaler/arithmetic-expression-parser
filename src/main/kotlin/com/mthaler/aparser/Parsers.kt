package com.mthaler.aparser

import com.mthaler.aparser.util.Result

fun interface Parser<T> {

    fun parse(input: String): Result<T>

    operator fun invoke(input: String): Result<T> = parse(input)
}

// combinators

fun <T1, T2> sequence(p1: Parser<T1>, p2: Parser<T2>): Parser<Pair<T1, T2>> = Parser { input ->
    p1(input).flatMap { r1, rest -> p2(rest).map { r2 -> Pair(r1, r2) } }
}

fun <T> orderedChoice(p1: Parser<T>, p2: Parser<T>): Parser<T> = Parser { input ->
    when(val r1 = p1(input)) {
        is Result.OK -> r1
        is Result.Err -> p2(input).mapExpected { e ->  "${r1.expected} or $e" }
    }
}

fun <T> optional(p: Parser<T>): Parser<T?> = Parser { input ->
    when(val r = p(input)) {
        is Result.OK -> r
        is Result.Err -> Result.OK(null, input)
    }
}

fun <T> zeroOrMore(p: Parser<T>): Parser<List<T>> = Parser { input ->
    var t = input
    var done = false
    val result = ArrayList<T>()
    while(!done) {
        when(val r = p(t)) {
            is Result.OK -> {
                result.add(r.value)
                t = r.rest
            }
            is Result.Err -> done = true
        }
    }
    Result.OK(result, t)
}

fun <T> oneOrMore(p: Parser<T>): Parser<List<T>> = Parser { input ->

    var t = input
    var done = false
    val result = ArrayList<T>()
    var err: Result.Err? = null

    while (!done) {
        when (val r = p(t)) {
            is Result.OK -> {
                result.add(r.value)
                t = r.rest
            }
            is Result.Err -> {
                if (result.isEmpty())
                err = r
                done = true
            }
        }
    }
    if (err != null) {
        err
    } else {
        Result.OK(result, t)
    }
}

fun <T, U> Parser<T>.map(f: (T) -> U): Parser<U> = Parser { input ->
    this(input).map(f)
}

fun <T, U> Parser<T>.means(u: U): Parser<U> = map { u }

infix fun <T1, T2> Parser<T1>.and(p2: Parser<T2>): Parser<Pair<T1, T2>> =
    sequence(this, p2)

infix fun <T> Parser<T>.or(p2: Parser<T>): Parser<T> =
    orderedChoice(this, p2)
