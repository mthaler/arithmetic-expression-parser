package com.mthaler.parser.ast

import com.mthaler.parser.Result
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

    "expression" {
        //val e = expression()
        //e("3.14") shouldBe Result.OK(Expr.Number(3.14), "")
        //e("3+4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), "")
        //e("3 + 4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3.0), Expr.Number(4.0), "+"), "")
        //e("3 + 4 + 5") shouldBe Result.OK(Expression.BinOp(Expression.Number(3.0), Expression.Number(4.0), Terminal.BinaryOperator("+")), "")
        //e("(3 + 4)") shouldBe Result.OK(Expression.BinOp(Expression.Number(3.0), Expression.Number(4.0), Terminal.BinaryOperator("+")), "")
    }
})