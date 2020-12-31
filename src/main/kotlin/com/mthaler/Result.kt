package com.mthaler

sealed class Result<out T> {
    data class OK<T>(val value: T, val rest: String): Result<T>()
    data class Err(val expected: String, val input: String): Result<Nothing>()
}