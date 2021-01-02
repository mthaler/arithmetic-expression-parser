package com.mthaler.parser.arithmetic

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.identifier
import com.mthaler.parser.tokens.whitespaces
import kotlin.math.exp

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

// terminals

val number: Parser<Expr> = ws(tnumber).map { Expr.Number(it.toDouble()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))
val times = ws(charLiteral('*'))
val div = ws(charLiteral('/'))
val exp = ws(charLiteral('^'))

// unary -
val neg = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))

val funcname = identifier

object Expression: RecursiveParser<Expr>() {

    init {

        val func: Parser<Expr> = (funcname and lpar and this and rpar).map { Expr.UnaryOp(it.first.second, it.first.first.first) }

        val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

        val factor: Parser<Expr> = (neg and number).map { Expr.UnaryOp(it.second, it.first) as Expr } or number

        val operand: Parser<Expr> = factor or group or func

        val power: Parser<Expr> = (operand and zeroOrMore(exp and operand)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first) }
        }

        val term: Parser<Expr> = (power and zeroOrMore((times or div) and power)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first) }
        }

        val expr: Parser<Expr> = (term and zeroOrMore((plus or minus) and operand)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first) }
        }

        this.parser = expr or number
    }
}