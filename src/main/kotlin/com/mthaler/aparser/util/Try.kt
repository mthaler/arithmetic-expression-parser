package com.mthaler.aparser.util

sealed class Try<out T> {
    data class Success<T>(val value: T): Try<T>()
    data class Failure(val ex: Exception): Try<Nothing>()

    fun <U> map(f: (T) -> U): Try<U> = when(this) {
        is Failure -> this
        is Success -> Success(f(value))
    }

    fun <U> flatMap(f: (T) -> Try<U>): Try<U> = when(this) {
        is Failure -> this
        is Success -> f(value)
    }

    companion object {
        fun <U>invoke(f: () -> U): Try<U> {
            try {
                val result = f()
                return Success(result)
            } catch (ex: Exception) {
                return Failure(ex)
            }
        }
    }
}