package com.mthaler.parser.ast

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.whitespaces

interface ExprParser: Parser<Expr>

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

// terminals

val number = ws(tnumber).map { Expr.Number(it.toDouble()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))
