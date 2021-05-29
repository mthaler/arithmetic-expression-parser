package com.mthaler.aparser.dice

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import com.mthaler.aparser.util.Result

class ParsersTest: StringSpec({

    "die" {
        die("") shouldBe Result.Err("'d'", "")
        die("d6") shouldBe Result.OK(Expr.Die(6, 1), "")
        die("2d6") shouldBe Result.OK(Expr.Die(6, 2), "")
        die("d6foo") shouldBe Result.OK(Expr.Die(6, 1), "foo")
        die("2d6foo") shouldBe Result.OK(Expr.Die(6, 2), "foo")
    }

    "number" {
        number("123") shouldBe Result.OK(Expr.Number(123), "")
        number("123foo") shouldBe Result.OK(Expr.Number(123), "foo")
        number("123 foo") shouldBe Result.OK(Expr.Number(123), "foo")
        number("foo") shouldBe Result.Err("digits", "foo")
        number("foo123") shouldBe Result.Err("digits", "foo123")
    }

    "expression" {
        val e = Expression
        e("3") shouldBe Result.OK(Expr.Number(3), "")
        e("-3") shouldBe Result.OK(Expr.UnaryOp(Expr.Number(3), "-"), "")
        e("3+4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3), Expr.Number(4), "+"), "")
        e("3 + 4") shouldBe Result.OK(Expr.BinOp(Expr.Number(3), Expr.Number(4), "+"), "")
        e("3 + (4 + 5)") shouldBe Result.OK(Expr.BinOp(Expr.Number(3), Expr.BinOp(Expr.Number(4),Expr.Number(5), "+"), "+"), "")
        e("(3 + 4) + 5") shouldBe Result.OK(Expr.BinOp(Expr.BinOp(Expr.Number(3), Expr.Number(4), "+"), Expr.Number(5), "+"), "")
        e("3 + 4 + 5") shouldBe Result.OK(Expr.BinOp(Expr.BinOp(Expr.Number(3), Expr.Number(4), "+"), Expr.Number(5), "+"), "")
    }
})