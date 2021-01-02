package com.mthaler.parser.arithmetic

import com.mthaler.parser.*
import com.mthaler.parser.tokens.number as tnumber
import com.mthaler.parser.tokens.charLiteral
import com.mthaler.parser.tokens.whitespaces

fun <T>ws(p: Parser<T>): Parser<T> = (optional(whitespaces) and p and optional(whitespaces)).map { it.middle() }

// terminals

val number: Parser<Expr> = ws(tnumber).map { Expr.Number(it.toDouble()) }

val plus = ws(charLiteral('+'))
val minus = ws(charLiteral('-'))

val lpar = ws(charLiteral('('))
val rpar = ws(charLiteral(')'))

object Expression: RecursiveParser<Expr>() {

    init {

        val group = (lpar and this and rpar).map { it.middle() }

        val operand = number or group

        val term: Parser<Expr> = (operand and (plus or minus) and operand).map { p ->
            val ex1 = p.first.first
            val op = p.first.second
            val ex2 = p.second
            Expr.BinOp(ex1, ex2, op)
        }

        this.parser = term or number
    }
}