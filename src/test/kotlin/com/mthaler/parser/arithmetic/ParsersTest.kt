package com.mthaler.parser.arithmetic

import com.mthaler.parser.Result
import com.mthaler.parser.and
import com.mthaler.parser.tokens.charLiteral
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "ws" {
        val n = ws(com.mthaler.parser.tokens.number)
        val plus = ws(charLiteral('+'))
        val p = n and plus and n
        p("3 + 4") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "")
        p(" 3 + 4 ") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "")
        p("3 + 4foo") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "foo")
        p("3 + 4 foo") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "foo")
    }

    "number" {
        number("123") shouldBe Result.OK(Expr.Number(123.0), "")
        number("123foo") shouldBe Result.OK(Expr.Number(123.0), "foo")
        number("123 foo") shouldBe Result.OK(Expr.Number(123.0), "foo")
        number("3.14") shouldBe Result.OK(Expr.Number(3.14), "")
        number(" 3.14 ") shouldBe Result.OK(Expr.Number(3.14), "")
        number("3.14foo") shouldBe Result.OK(Expr.Number(3.14), "foo")
        number("foo") shouldBe Result.Err("number", "foo")
        number("foo123") shouldBe Result.Err("number", "foo123")
    }

    "+" {
        plus("+") shouldBe Result.OK("+", "")
        plus("+ 5") shouldBe Result.OK("+", "5")
        plus("foo") shouldBe Result.Err("'+'", "foo")
    }

    "-" {
        minus("-") shouldBe Result.OK("-", "")
        minus(" - ") shouldBe Result.OK("-", "")
        minus("- 5") shouldBe Result.OK("-", "5")
        minus("foo") shouldBe Result.Err("'-'", "foo")
    }

    "*" {
        times("*") shouldBe Result.OK("*", "")
        times("* 5") shouldBe Result.OK("*", "5")
        times("foo") shouldBe Result.Err("'*'", "foo")
    }

    "/" {
        div("/") shouldBe Result.OK("/", "")
        div("/ 5") shouldBe Result.OK("/", "5")
        div("foo") shouldBe Result.Err("'/'", "foo")
    }

    "expression" {
        val e = Expression
        e("3.14") shouldBe Result.OK(Expr.Number(3.14), "")
        e("-3.14") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "-"), "")
        e("3+4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), "")
        e("3 + 4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), "")
        e("3 + (4 + 5)") shouldBe Result.OK(Expr.BinOp(Expr.Number(3.0), Expr.BinOp(Expr.Number(4.0), Expr.Number(5.0), "+"), "+"), "")
        e("(3 + 4) + 5") shouldBe Result.OK(Expr.BinOp(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), Expr.Number(5.0), "+"), "")
        e("3 + 4 + 5") shouldBe Result.OK(Expr.BinOp(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), Expr.Number(5.0), "+"), "")
    }
})