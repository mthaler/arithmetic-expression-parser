package com.mthaler

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParserTest: StringSpec({

    "dot" {
        dot(".foo") shouldBe Result.OK(Unit, "foo")
        dot("foo") shouldBe Result.Err("a dot", "foo")
    }
})