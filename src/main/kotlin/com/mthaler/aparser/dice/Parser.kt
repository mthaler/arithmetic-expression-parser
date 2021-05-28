package com.mthaler.aparser.dice

import com.mthaler.aparser.*
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.tokens.whitespaces

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

// terminals

val integer: Parser<Expr>  = ws(com.mthaler.aparser.tokens.digits).map { Expr.Number(it.toInt()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))
val times = ws(charLiteral('*'))
val div = ws(charLiteral('/'))

// unary -
val neg = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))

object Expression: RecursiveParser<Expr>() {

    val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

    val factor: Parser<Expr> = (neg and integer).map { Expr.UnaryOp(it.second, it.first) as Expr } or integer
}