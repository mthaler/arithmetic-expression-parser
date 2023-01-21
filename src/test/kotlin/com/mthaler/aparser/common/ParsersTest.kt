package com.mthaler.aparser.common

import com.mthaler.aparser.and
import com.mthaler.aparser.tokens.charLiteral
import com.mthaler.aparser.util.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "ws" {
        val n = ws(com.mthaler.aparser.tokens.number)
        val plus = ws(charLiteral('+'))
        val p = n and plus and n
        p("3 + 4") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "")
        p(" 3 + 4 ") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "")
        p("3 + 4foo") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "foo")
        p("3 + 4 foo") shouldBe Result.OK(Pair(Pair("3", "+"), "4"), "foo")
    }

    "+" {
        plus("+") shouldBe Result.OK("+", "")
        plus("+ 5") shouldBe Result.OK("+", "5")
        plus("foo") shouldBe Result.Err("'+'", "foo")
    }

    "-" {
        minus("-") shouldBe Result.OK("-", "")
        minus(" - ") shouldBe Result.OK("-", "")
        minus("- 5") shouldBe Result.OK("-", "5")
        minus("foo") shouldBe Result.Err("'-'", "foo")
    }

    "*" {
        times("*") shouldBe Result.OK("*", "")
        times("* 5") shouldBe Result.OK("*", "5")
        times("foo") shouldBe Result.Err("'*'", "foo")
    }

    "/" {
        div("/") shouldBe Result.OK("/", "")
        div("/ 5") shouldBe Result.OK("/", "5")
        div("foo") shouldBe Result.Err("'/'", "foo")
    }
})