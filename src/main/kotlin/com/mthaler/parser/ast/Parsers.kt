package com.mthaler.parser.ast

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.whitespaces

typealias ExprParser = (String) -> Result<Expr>

fun <T>ws(p: Parser<T>): Parser<T> = (optional(::whitespaces) and p and optional(::whitespaces)).map { it.middle() }

// terminals

val number = ws(::tnumber).map { Expr.Number(it.toDouble()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))

fun term(): ExprParser = (operand() and (plus or minus) and operand()).map { p ->
    val ex1 = p.first.first
    val op = p.first.second
    val ex2 = p.second
    Expr.BinOp(ex1, ex2, op)
}

fun group(): ExprParser = (lpar and expression() and rpar).map { it.middle() }

fun operand(): ExprParser = number

fun expression(): ExprParser = term() or operand()