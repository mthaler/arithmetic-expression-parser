package com.mthaler.parser.ast

import com.mthaler.parser.Result
import com.mthaler.parser.map
import com.mthaler.parser.tokens.number as tnumber

typealias ExpressionParser = (String) -> Result<Expression>

val number: ExpressionParser = ::tnumber.map { Expression.Number(it.toDouble()) }