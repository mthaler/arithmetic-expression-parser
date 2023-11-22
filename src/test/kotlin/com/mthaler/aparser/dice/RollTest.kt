package com.mthaler.aparser.dice

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RollTest: StringSpec({

    "d" {
        val die0 = object : Roll {
            override fun d(sides: Int): Int = 1
        }
        die0.d(4) shouldBe 1
        val die1 = object : Roll {
            override fun d(sides: Int): Int = 4
        }
        die1.d(4) shouldBe 4
    }
})