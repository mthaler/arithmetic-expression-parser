package com.mthaler.aparser.common

import com.mthaler.aparser.*
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.tokens.whitespaces

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))
val times = ws(charLiteral('*'))
val div = ws(charLiteral('/'))
val exp = ws(charLiteral('^'))

// unary -
val neg = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))