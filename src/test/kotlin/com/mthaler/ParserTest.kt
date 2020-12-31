package com.mthaler

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParserTest: StringSpec({

    "dot" {
        dot(".foo") shouldBe Result.OK(Unit, "foo")
        dot("foo") shouldBe Result.Err("a dot", "foo")
    }

    "integer" {
        integer("123foo") shouldBe Result.OK(123, "foo")
        integer("foo") shouldBe Result.Err("an integer", "foo")
    }
})