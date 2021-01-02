package com.mthaler.parser.arith

import com.mthaler.parser.Result
import java.lang.IllegalArgumentException

fun Result<Expr>.eval(): Result<Double> = when(this) {
    is Result.OK ->  Result.OK(eval(this.value), this.rest)
    is Result.Err -> this
}

private fun eval(value: Expr): Double = when(value) {
    is Expr.Number -> value.number
    is Expr.BinOp -> when(val op = value.operator) {
        "+" -> eval(value.operand1) + eval(value.operand2)
        "-" -> eval(value.operand1) - eval(value.operand2)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
}