package com.mthaler.parser.arithmetic

import com.mthaler.parser.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EvalTest: StringSpec({

    "expression" {
        val e = Expression
        e("3.14").eval() shouldBe Result.OK(3.14, "")
        e("3+4").eval() shouldBe Result.OK(7.0, "")
        e("3 + 4").eval() shouldBe Result.OK(7.0, "")
        e("3 - 4").eval() shouldBe Result.OK(-1.0, "")
        e("3 + (4 + 5)").eval() shouldBe Result.OK(12.0, "")
        e("(3 + 4) + 5").eval() shouldBe Result.OK(12.0, "")
        e("3 * 4").eval() shouldBe Result.OK(12.0, "")
        e("3 * 4 + 2").eval() shouldBe Result.OK(14.0, "")
        //e("2 + 3 * 4").eval() shouldBe Result.OK(14.0, "")
    }
})