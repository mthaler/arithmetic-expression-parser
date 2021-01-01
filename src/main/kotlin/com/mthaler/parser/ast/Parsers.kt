package com.mthaler.parser.ast

import com.mthaler.parser.Result
import com.mthaler.parser.and
import com.mthaler.parser.map
import com.mthaler.parser.or
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral

typealias ExpressionParser = (String) -> Result<Expression>

// terminals

val number = ::tnumber.map { Terminal.Number(it.toDouble()) }

val plus = charLiteral('+').map { Terminal.BinaryOperator(it) }
val minus = charLiteral('-').map { Terminal.BinaryOperator(it) }
val times = charLiteral('*').map { Terminal.BinaryOperator(it) }
val div = charLiteral('/').map { Terminal.BinaryOperator(it) }

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