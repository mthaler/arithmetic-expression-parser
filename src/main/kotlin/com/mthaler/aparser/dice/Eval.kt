package com.mthaler.aparser.dice

import com.mthaler.aparser.common.rpar
import com.mthaler.aparser.util.ParseException
import com.mthaler.aparser.util.Result
import java.lang.IllegalArgumentException

/*
 * Evaluates the given expression. This method can throw an exception if e.g. an undefined function name is used
 */
fun Result<Expr>.eval(roll: Roll = DefaultRoll()): Result<Int> = map { eval(it, roll) }

/**
 * Evaluates the given expression. This method will return a try, it will not throw an exception if the expression cannot be evaluated
 */
fun Result<Expr>.tryEval(roll: Roll = DefaultRoll()): kotlin.Result<Int> = when(this) {
    is Result.OK -> runCatching { eval(this.value, roll) }
    is Result.Err -> kotlin.Result.failure(ParseException(this.expected, this.input))
}

/**
 * Evaluates the given expression
 */
private fun eval(expr: Expr, roll: Roll): Int = when(expr) {
    is Expr.Number -> expr.number
    is Expr.Die -> {
        var result = 0
        var x = 0
        while (x < expr.count) {
            result += roll.d(expr.sides)
            x += 1
        }
        result
    }
    is Expr.UnaryOp -> when(val op = expr.operator) {
        "-" -> -eval(expr.operand, roll)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    is Expr.BinOp -> when(val op = expr.operator) {
        "+" -> eval(expr.operand1, roll) + eval(expr.operand2, roll)
        "-" -> eval(expr.operand1, roll) - eval(expr.operand2, roll)
        "*" -> eval(expr.operand1, roll) * eval(expr.operand2, roll)
        "/" -> eval(expr.operand1, roll) / eval(expr.operand2, roll)
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
}