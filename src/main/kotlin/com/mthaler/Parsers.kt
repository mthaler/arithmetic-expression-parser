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