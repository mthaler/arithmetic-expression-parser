package com.mthaler

sealed class Expression {

    data class FromNumber(val number: Terminal.Number): Expression()
    data class FromBinary(val operand1: Expression, val operand2: Expression, val bin: BinaryOperator): Expression()
}