package com.mthaler.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "charLiteral" {
        val p = charLiteral('.')
        p(".foo") shouldBe Result.OK('.', "foo")
        p("foo") shouldBe Result.Err("a '.'", "foo")
    }

    "stringLiteral" {
        val p = stringLiteral("foo")
        p("foobar") shouldBe Result.OK("foo", "bar")
        p("boofar") shouldBe Result.Err("'foo'", "boofar")
    }

    "whitespaces" {
        whitespace(" 123") shouldBe Result.OK(Unit, "123")
        whitespace("  123") shouldBe Result.OK(Unit, "123")
        whitespace(" \t123") shouldBe Result.OK(Unit, "123")
        whitespace("123") shouldBe Result.Err("not a whitespace", "123")
    }
})