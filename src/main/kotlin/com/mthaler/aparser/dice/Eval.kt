package com.mthaler.aparser.dice

import com.mthaler.aparser.util.ParseException
import com.mthaler.aparser.util.Result
import com.mthaler.aparser.util.Try

/*
 * Evaluates the given expression. This method can throw an exception if e.g. an undefined function name is used
 */
fun Result<Expr>.eval(): Result<Int> = map { eval(it) }

/**
 * Evaluates the given expression. This method will return a try, it will not throw an exception if the expression cannot be evaluated
 */
fun Result<Expr>.tryEval(): Try<Int> = when(this) {
    is Result.OK -> Try { eval(this.value) }
    is Result.Err -> Try.Failure(ParseException(this.expected, this.input))
}

/**
 * Evaluates the given expression
 */
private fun eval(expr: Expr): Int = when(expr) {
    is Expr.Number -> expr.number
    else -> TODO()
}