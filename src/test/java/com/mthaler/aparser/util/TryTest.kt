package com.mthaler.aparser.util

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.lang.Exception

class TryTest: StringSpec({

    "map" {
        val t0 = Try.Success(42)
        val t1 = t0.map { it.toString() }
        t1 shouldBe Try.Success("42")
        val t2: Try<Int> = Try.Failure(Exception("test"))
        val t3 = t2.map { it.toString() }
        t3 shouldBe t2
    }

    "invoke" {
        val t0 = Try { 42 }
        t0 shouldBe Try.Success(42)
        val t1 = Try { throw Exception("test") }
        (t1 is Try.Failure) shouldBe true
    }
})