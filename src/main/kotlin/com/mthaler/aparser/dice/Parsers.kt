package com.mthaler.aparser.dice

import com.mthaler.aparser.*
import com.mthaler.aparser.common.ws
import com.mthaler.aparser.common.*
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.tokens.digits

val number: Parser<Expr>  = ws(digits).map { Expr.Number(it.text.toInt()) }

val die: Parser<Expr> = ws(optional(digits) and charLiteral('d') and digits).map {
    val optionalCount = it.first.first
    val sides = it.second
    Expr.Die(sides.text.toInt(), optionalCount?.text?.toInt() ?: 1)
}

object Expression: RecursiveParser<Expr>() {

    init {
        val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

        val factor: Parser<Expr> = (neg and number).map { Expr.UnaryOp(it.second, it.first.text) as Expr } or number

        val operand: Parser<Expr> = die or factor or group

        val power: Parser<Expr> = (operand and zeroOrMore(exp and operand)).map { p ->
            if (p.second.isEmpty()) {
                p.first
            } else {
                val rest = p.second
                var e = p.first
                val result = arrayListOf<Pair<String, Expr>>()
                for (item in rest) {
                    result.add(Pair(item.first.text, e))
                    e = item.second
                }
                result.foldRight(e) { item, expr -> Expr.BinOp(item.second, expr, item.first) }
            }
        }

        val term: Parser<Expr> = (power and zeroOrMore((times or div) and power)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first.text) }
        }

        val expr: Parser<Expr> = (term and zeroOrMore((plus or minus) and term)).map { p ->
            p.second.fold(p.first) { expr, item -> Expr.BinOp(expr, item.second, item.first.text) }
        }

        this.parser = expr or die or number
    }
}