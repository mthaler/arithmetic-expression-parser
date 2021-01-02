package com.mthaler.parser.arithmetic

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.whitespaces
import kotlin.math.exp

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

// terminals

val number: Parser<Expr> = ws(tnumber).map { Expr.Number(it.toDouble()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))
val times = ws(charLiteral('*'))
val div = ws(charLiteral('/'))

// unary -
val neg = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))

object Expression: RecursiveParser<Expr>() {

    init {

        val group = (lpar and this and rpar).map { it.middle() }

        val factor: Parser<Expr> = (neg and number).map { Expr.UnaryOp(it.second, it.first) as Expr } or number

        val operand: Parser<Expr> = factor or group

        val term: Parser<Expr> = (operand and zeroOrMore((times or div) and operand)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first) }
        }

        val expr: Parser<Expr> = (term and zeroOrMore((plus or minus) and operand)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first) }
        }

        this.parser = expr or number
    }
}