package com.mthaler.aparser.arithmetic

import com.mthaler.aparser.Result
import java.lang.IllegalArgumentException
import kotlin.math.*

fun Result<Expr>.eval(context: Context = Context.Empty): Result<Double> = map { eval(it, context) }

/**
 * Evaluates the given expression
 */
private fun eval(expr: Expr, context: Context): Double = when(expr) {
    is Expr.Number -> expr.number
    is Expr.GlobalVar -> context.globalVars[expr.name] ?: throw UndefinedVariableException(expr.name)
    is Expr.UnaryOp -> when(val op = expr.operator) {
        "-" -> -eval(expr.operand, context)
        "abs" -> abs(eval(expr.operand, context))
        "cos" -> cos(eval(expr.operand, context))
        "sin" -> sin(eval(expr.operand, context))
        "tan" -> tan(eval(expr.operand, context))
        "acos" -> acos(eval(expr.operand, context))
        "asin" -> asin(eval(expr.operand, context))
        "atan" -> atan(eval(expr.operand, context))
        "cosh" -> cosh(eval(expr.operand, context))
        "sinh" -> sinh(eval(expr.operand, context))
        "tanh" -> tanh(eval(expr.operand, context))
        "exp" -> exp(eval(expr.operand, context))
        "ln" -> ln(eval(expr.operand, context))
        "log" -> log10(eval(expr.operand, context))
        "sqrt" -> sqrt(eval(expr.operand, context))
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
    is Expr.BinOp -> when(val op = expr.operator) {
        "+" -> eval(expr.operand1, context) + eval(expr.operand2, context)
        "-" -> eval(expr.operand1, context) - eval(expr.operand2, context)
        "*" -> eval(expr.operand1, context) * eval(expr.operand2, context)
        "/" -> eval(expr.operand1, context) / eval(expr.operand2, context)
        "%" -> eval(expr.operand1, context) % eval(expr.operand2, context)
        "^" -> eval(expr.operand1, context).pow(eval(expr.operand2, context))
        else -> throw IllegalArgumentException("Unknown operator: $op")
    }
}