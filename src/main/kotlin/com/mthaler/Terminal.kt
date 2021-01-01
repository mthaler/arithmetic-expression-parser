package com.mthaler

sealed class Terminal {

    data class Number(val value: kotlin.Number): Terminal()

    object Plus: Terminal(), BinaryOperator
    object Minus: Terminal(), BinaryOperator
    object Times: Terminal(), BinaryOperator
    object Div: Terminal(), BinaryOperator
}

