package com.mthaler.parser.arithmetic

import com.mthaler.parser.Result
import java.lang.IllegalArgumentException

fun Result<Expr>.eval(): Result<Double> = map { eval(it) }

private fun eval(value: Expr): Double = when(value) {
    is Expr.Number -> value.number
    is Expr.UnaryOp -> when(val op = value.operator) {
        "-" -> -eval(value.operand)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    is Expr.BinOp -> when(val op = value.operator) {
        "+" -> eval(value.operand1) + eval(value.operand2)
        "-" -> eval(value.operand1) - eval(value.operand2)
        "*" -> eval(value.operand1) * eval(value.operand2)
        "/" -> eval(value.operand1) / eval(value.operand2)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
}