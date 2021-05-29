package com.mthaler.aparser.dice

import com.mthaler.aparser.*
import com.mthaler.aparser.common.ws
import com.mthaler.aparser.common.*

val integer: Parser<Expr>  = ws(com.mthaler.aparser.tokens.digits).map { Expr.Number(it.toInt()) }

object Expression: RecursiveParser<Expr>() {

    val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

    val factor: Parser<Expr> = (neg and integer).map { Expr.UnaryOp(it.second, it.first) as Expr } or integer
}