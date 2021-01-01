package com.mthaler.parser.ast

import com.mthaler.parser.Result
import com.mthaler.parser.map
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral

typealias ExpressionParser = (String) -> Result<Expression>

val number = ::tnumber.map { Terminal.Number(it.toDouble()) }

val plus = charLiteral('+').map { Terminal.BinaryOperator(it) }