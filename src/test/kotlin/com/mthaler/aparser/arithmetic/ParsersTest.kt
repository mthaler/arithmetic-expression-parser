package com.mthaler.aparser.arithmetic

import com.mthaler.aparser.util.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

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
        globalVar("123") shouldBe Result.Err("upper case letters", "123")
        globalVar("abc") shouldBe Result.Err("upper case letters", "abc")
        globalVar("aABC") shouldBe Result.Err("upper case letters", "aABC")
        globalVar("1ABC") shouldBe Result.Err("upper case letters", "1ABC")
        globalVar("A") shouldBe Result.OK(Expr.GlobalVar("A"), "")
        globalVar("AB") shouldBe Result.OK(Expr.GlobalVar("AB"), "")
        globalVar("ABC") shouldBe Result.OK(Expr.GlobalVar("ABC"), "")
        globalVar("AB1") shouldBe Result.OK(Expr.GlobalVar("AB"), "1")
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
        e("abs(-42)") shouldBe Result.OK(Expr.UnaryOp(Expr.UnaryOp(Expr.Number(42.0), "-"), "abs"), "")
        e("cos(3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "cos"), "")
        e("sin(3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin (3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin( 3.14 )") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("sin ( 3.14 )") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "sin"), "")
        e("tan(3.14)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3.14), "tan"), "")
        e("acos(1.0)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(1.0), "acos"), "")
        e("asin(1.0)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(1.0), "asin"), "")
        e("atan(1.0)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(1.0), "atan"), "")
        e("sqrt(4)") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(4.0), "sqrt"), "")

    }
})