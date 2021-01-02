package com.mthaler.parser.arith

sealed class Expr {

    data class Number(val number: Double): Expr()
    data class BinOp(val operand1: Expr, val operand2: Expr, val operator: String): Expr()
}