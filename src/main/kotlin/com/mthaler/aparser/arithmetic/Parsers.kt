package com.mthaler.aparser.arithmetic

import com.mthaler.aparser.*
import com.mthaler.aparser.tokens.*
import com.mthaler.aparser.tokens.number as tnumber
import com.mthaler.aparser.common.*
import kotlin.math.PI
import kotlin.math.E

// terminals

val number: Parser<Expr> = ws(tnumber).map { Expr.Number(it.text.toDouble()) }

val pi: Parser<Expr> = (ws(stringLiteral("pi")) or ws(stringLiteral("\u03C0"))).map { Expr.Number(PI) }
val e: Parser<Expr> = ws(stringLiteral("e")).map { Expr.Number(E) }

val globalVar: Parser<Expr> = ws(upperCaseLetters).map { Expr.GlobalVar(it.text) }

val funcname = lowerCaseLetters or charLiteral('\u221A')

object Expression: RecursiveParser<Expr>() {

    init {

        val func: Parser<Expr> = (funcname and lpar and this and rpar).map { Expr.UnaryOp(it.first.second, it.first.first.first.text) }
        val group: Parser<Expr> = (lpar and this and rpar).map { it.middle() }

        val factor: Parser<Expr> = (neg and number).map { Expr.UnaryOp(it.second, it.first.text) as Expr } or number or pi or e

        val operand: Parser<Expr> = func or factor or globalVar or group

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

        this.parser = expr or number
    }
}
