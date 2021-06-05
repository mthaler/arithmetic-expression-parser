package com.mthaler.aparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class InputTest: StringSpec({

    "rest" {
        val input0 = Input("test", 0)
        input0.rest shouldBe "test"
        val input1 = input0.copy(position = 2)
        input1.rest shouldBe "st"
    }
})