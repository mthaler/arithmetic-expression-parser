package com.mthaler.parser.ast

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.whitespaces

typealias ExpressionParser = (String) -> Result<Expression>

val ws = optional(::whitespaces)

// terminals

val number = (ws and ::tnumber and ws).map { Terminal.Number(it.middle().toDouble()) }

val plus = (ws and charLiteral('+') and ws).map { Terminal.BinaryOperator(it.middle()) }
val minus = (ws and charLiteral('-') and ws).map { Terminal.BinaryOperator(it.middle()) }
val times = (ws and charLiteral('*') and ws).map { Terminal.BinaryOperator(it.middle()) }
val div = (ws and charLiteral('/') and ws).map { Terminal.BinaryOperator(it.middle()) }

val binaryOperator = plus or minus or times or div

fun expression(): ExpressionParser = { input ->

    val fromNumber = number.map { Expression.Number(it.value) }

    val group = (charLiteral('(') and expression() and charLiteral(')')).map { it.first.second }

    val fromBinary = ((fromNumber or group) and binaryOperator and ((fromNumber or group))).map { p ->
        val ex1 = p.first.first
        val op = p.first.second
        val ex2 = p.second
        Expression.BinOp(ex1, ex2, op)
    }

    (fromBinary or fromNumber)(input)
}