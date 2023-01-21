package com.mthaler.aparser.common

import com.mthaler.aparser.and
import com.mthaler.aparser.toBuffer
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.util.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "ws" {
        val n = ws(com.mthaler.aparser.tokens.number)
        val plus = ws(charLiteral('+'))
        val p = n and plus and n
        p("3 + 4") shouldBe Result.OK(Pair(Pair("3".toBuffer(), "+".toBuffer()), "4".toBuffer()), "")
        p(" 3 + 4 ") shouldBe Result.OK(Pair(Pair("3".toBuffer(), "+".toBuffer()), "4".toBuffer()), "")
        p("3 + 4foo") shouldBe Result.OK(Pair(Pair("3".toBuffer(), "+".toBuffer()), "4".toBuffer()), "foo")
        p("3 + 4 foo") shouldBe Result.OK(Pair(Pair("3".toBuffer(), "+".toBuffer()), "4".toBuffer()), "foo")
    }

    "+" {
        plus("+") shouldBe Result.OK("+".toBuffer(), "")
        plus("+ 5") shouldBe Result.OK("+".toBuffer(), "5")
        plus("foo") shouldBe Result.Err("'+'", "foo")
    }

    "-" {
        minus("-") shouldBe Result.OK("-".toBuffer(), "")
        minus(" - ") shouldBe Result.OK("-".toBuffer(), "")
        minus("- 5") shouldBe Result.OK("-".toBuffer(), "5")
        minus("foo") shouldBe Result.Err("'-'", "foo")
    }

    "*" {
        times("*") shouldBe Result.OK("*".toBuffer(), "")
        times("* 5") shouldBe Result.OK("*".toBuffer(), "5")
        times("foo") shouldBe Result.Err("'*'", "foo")
    }

    "/" {
        div("/") shouldBe Result.OK("/".toBuffer(), "")
        div("/ 5") shouldBe Result.OK("/".toBuffer(), "5")
        div("foo") shouldBe Result.Err("'/'", "foo")
    }
})