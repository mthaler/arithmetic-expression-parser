package com.mthaler.aparser.dice

import com.mthaler.aparser.util.ParseException
import com.mthaler.aparser.util.Result
import com.mthaler.aparser.util.Try
import java.lang.IllegalArgumentException

/*
 * Evaluates the given expression. This method can throw an exception if e.g. an undefined function name is used
 */
fun Result<Expr>.eval(rng: RNG = RNG.Default): Result<Int> = map { eval(it, rng) }

/**
 * Evaluates the given expression. This method will return a try, it will not throw an exception if the expression cannot be evaluated
 */
fun Result<Expr>.tryEval(rng: RNG = RNG.Default): Try<Int> = when(this) {
    is Result.OK -> Try { eval(this.value, rng) }
    is Result.Err -> Try.Failure(ParseException(this.expected, this.input))
}

/**
 * Evaluates the given expression
 */
private fun eval(expr: Expr, rng: RNG): Int = when(expr) {
    is Expr.Number -> expr.number
    is Expr.Die -> {
        var result = 0
        for (i in 0 .. expr.count) {
            result += rng.nextInt(expr.sides) + 1
        }
        result
    }
    is Expr.UnaryOp -> when(val op = expr.operator) {
        "-" -> -eval(expr.operand, rng)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    is Expr.BinOp -> when(val op = expr.operator) {
        "+" -> eval(expr.operand1, rng) + eval(expr.operand2, rng)
        "-" -> eval(expr.operand1, rng) - eval(expr.operand2, rng)
        "*" -> eval(expr.operand1, rng) * eval(expr.operand2, rng)
        "/" -> eval(expr.operand1, rng) / eval(expr.operand2, rng)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
}