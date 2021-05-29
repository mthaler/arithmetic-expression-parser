package com.mthaler.aparser.dice

import com.mthaler.aparser.*
import com.mthaler.aparser.common.ws
import com.mthaler.aparser.common.*
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.tokens.digits

val integer: Parser<Expr>  = ws(digits).map { Expr.Number(it.toInt()) }

val die: Parser<Expr> = ws(optional(digits) and charLiteral('d') and digits).map {
    val optionalCount = it.first.first
    val sides = it.second
    Expr.Die(sides.toInt(), optionalCount?.toInt() ?: 1)
}

object Expression: RecursiveParser<Expr>() {

    val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

    val factor: Parser<Expr> = (neg and integer).map { Expr.UnaryOp(it.second, it.first) as Expr } or integer
}