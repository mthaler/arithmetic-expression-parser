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
})