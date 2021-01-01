package com.mthaler.parser.ast

sealed class Expression {

    data class Number(val number: Double): Expression()
    data class BinOp(val operand1: Expression, val operand2: Expression, val operator: BinaryOperator): Expression()
}