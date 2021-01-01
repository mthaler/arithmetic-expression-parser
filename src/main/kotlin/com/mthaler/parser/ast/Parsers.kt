package com.mthaler.parser.ast

import com.mthaler.parser.Result
import com.mthaler.parser.map
import com.mthaler.parser.or
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral

typealias TerminalParser = (String) -> Result<Terminal>

typealias ExpressionParser = (String) -> Result<Expression>

// terminals

val number: TerminalParser = ::tnumber.map { Terminal.Number(it.toDouble()) }

val plus: TerminalParser = charLiteral('+').map { Terminal.BinaryOperator(it) }
val minus: TerminalParser = charLiteral('-').map { Terminal.BinaryOperator(it) }
val times: TerminalParser = charLiteral('*').map { Terminal.BinaryOperator(it) }
val div: TerminalParser = charLiteral('/').map { Terminal.BinaryOperator(it) }

val binaryOperator = plus or minus or times or div