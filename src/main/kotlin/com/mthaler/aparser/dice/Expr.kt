package com.mthaler.aparser.dice

sealed class Expr {
    data class Number(val number: Int): Expr()
    data class Die(val sides: Int): Expr()
    data class UnaryOp(val operand: Expr, val operator: String): Expr()
    data class BinOp(val operand1: Expr, val operand2: Expr, val operator: String): Expr()
}