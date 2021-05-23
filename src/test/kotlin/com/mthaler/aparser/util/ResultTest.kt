package com.mthaler.aparser.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ResultTest: StringSpec({
    "map" {
        val r0 = Result.OK(42, "rest")
        val r1 = r0.map { it.toString() }
        r1 shouldBe Result.OK("42", "rest")
        val r2: Result<Int> = Result.Err("sin", "")
        val r3 = r2.map { it.toString() }
        r3 shouldBe r2
    }
})