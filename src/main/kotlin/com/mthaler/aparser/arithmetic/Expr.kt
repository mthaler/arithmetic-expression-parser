package com.mthaler.aparser.arithmetic

sealed class Expr {
    data class Number(val number: Double): Expr()
    data class GlobalVar(val name: String): Expr()
    data class UnaryOp(val operand: Expr, val operator: String): Expr()
    data class BinOp(val operand1: Expr, val operand2: Expr, val operator: String): Expr()
}
