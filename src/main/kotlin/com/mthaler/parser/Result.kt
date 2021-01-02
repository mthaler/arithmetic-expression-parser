package com.mthaler.parser

sealed class Result<out T> {
    data class OK<T>(val value: T, val rest: String): Result<T>()
    data class Err(val expected: String, val input: String): Result<Nothing>() {
        init {
            println(this)
        }
    }

    fun <U> map(f: (T) -> U): Result<U> = when(this) {
        is Err -> this
        is OK -> OK(f(value), rest)
    }

    fun <U> flatMap(f: (T, String) -> Result<U>): Result<U> = when(this) {
        is Err -> this
        is OK -> f(value, rest)
    }

    fun mapExpected(f: (String) -> String): Result<T> = when(this) {
        is OK -> this
        is Err -> Err(f(expected), input)
    }
}