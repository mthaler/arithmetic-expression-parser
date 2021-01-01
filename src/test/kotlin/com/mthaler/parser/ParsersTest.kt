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

    "integer" {
        integer("1+2") shouldBe Result.OK(1, "+2")
        integer("12") shouldBe Result.OK(12, "")
        integer("123foo") shouldBe Result.OK(123, "foo")
        integer("foo") shouldBe Result.Err("an integer", "foo")
    }
})