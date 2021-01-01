package com.mthaler.parser.ast

sealed class Terminal {

    data class Number(val value: Double) : Terminal()
    data class BinaryOperator(val operator: String) : Terminal(), com.mthaler.parser.ast.BinaryOperator
}

