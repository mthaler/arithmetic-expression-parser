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
        "cos" -> evalTrigonometric(::cos, eval(expr.operand, context), context)
        "sin" -> evalTrigonometric(::sin, eval(expr.operand, context), context)
        "tan" -> evalTrigonometric(::tan, eval(expr.operand, context), context)
        "acos" -> evalInverseTrigonometric(::acos, eval(expr.operand, context), context)
        "asin" -> evalInverseTrigonometric(::asin, eval(expr.operand, context), context)
        "atan" -> evalInverseTrigonometric(::atan, eval(expr.operand, context), context)
        "cosh" -> evalTrigonometric(::cosh, eval(expr.operand, context), context)
        "sinh" -> evalTrigonometric(::sinh, eval(expr.operand, context), context)
        "tanh" -> evalTrigonometric(::tanh, eval(expr.operand, context), context)
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

private fun evalTrigonometric(f: (Double) -> Double, value: Double, context: Context): Double = when(context.trigonometricUnit) {
    TrigonometricUnit.Rad -> f(value)
    TrigonometricUnit.Degree -> f(value * PI / 180)
}

private fun evalInverseTrigonometric(f: (Double) -> Double, value: Double, context: Context): Double = when(context.trigonometricUnit) {
    TrigonometricUnit.Rad -> f(value)
    TrigonometricUnit.Degree -> f(value) * 180 / PI
}