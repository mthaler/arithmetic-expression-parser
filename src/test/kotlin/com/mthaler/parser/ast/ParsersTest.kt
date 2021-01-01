package com.mthaler.parser.ast

import com.mthaler.parser.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "number" {
        number("123") shouldBe Result.OK(Terminal.Number(123.0), "")
        number("123foo") shouldBe Result.OK(Terminal.Number(123.0), "foo")
        number("3.14") shouldBe Result.OK(Terminal.Number(3.14), "")
        number("3.14foo") shouldBe Result.OK(Terminal.Number(3.14), "foo")
        number("foo") shouldBe Result.Err("number", "foo")
        number("foo123") shouldBe Result.Err("number", "foo123")
    }

    "+" {
        plus("+") shouldBe Result.OK(Terminal.BinaryOperator("+"), "")
        plus("+ 5") shouldBe Result.OK(Terminal.BinaryOperator("+"), " 5")
        plus("foo") shouldBe Result.Err("'+'", "foo")
    }
})