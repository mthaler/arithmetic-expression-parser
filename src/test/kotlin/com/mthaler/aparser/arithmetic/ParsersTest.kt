package com.mthaler.aparser.arithmetic

import com.mthaler.aparser.Result
import com.mthaler.aparser.and
import com.mthaler.aparser.tokens.charLiteral
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "ws" {
        val n = ws(com.mthaler.aparser.tokens.number)
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
        number("10e3") shouldBe Result.OK(Expr.Number(10e3), "")
        number("10E3") shouldBe Result.OK(Expr.Number(10E3), "")
    }

    "globalVar" {
        globalVar("123") shouldBe Result.Err("'['", "123")
        globalVar("abc") shouldBe Result.Err("'['", "abc")
        globalVar("[abc") shouldBe Result.Err("']'", "")
        globalVar("[ab c]") shouldBe Result.Err("']'", " c]")
        globalVar("[a]") shouldBe Result.OK(Expr.GlobalVar("a"), "")
        globalVar("[ab]") shouldBe Result.OK(Expr.GlobalVar("ab"), "")
        globalVar("[abc]") shouldBe Result.OK(Expr.GlobalVar("abc"), "")
        globalVar("[1]") shouldBe Result.OK(Expr.GlobalVar("1"), "")
        globalVar("[12]") shouldBe Result.OK(Expr.GlobalVar("12"), "")
        globalVar("[123]") shouldBe Result.OK(Expr.GlobalVar("123"), "")
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
        e("2^3") shouldBe Result.OK(Expr.BinOp(Expr.Number(2.0), Expr.Number(3.0), "^"), "")
        e("2^3^4") shouldBe Result.OK(Expr.BinOp(Expr.Number(2.0), Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "^"), "^"), "")
    }

    "functions" {
        val e = Expression
        e("sin(3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin (3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin( 3.14 )") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin ( 3.14 )") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sqrt(4)").shouldBe(Result.OK(Expr.UnaryOp(Expr.Number(4.0), "sqrt"), ""))
    }
})