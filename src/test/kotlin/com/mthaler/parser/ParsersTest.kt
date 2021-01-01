package com.mthaler.parser

import com.mthaler.integer
import com.mthaler.string
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

    "digits" {
        digits("1") shouldBe Result.OK("1", "")
        digits("12") shouldBe Result.OK("12", "")
        digits("123") shouldBe Result.OK("123", "")
        digits("123foo") shouldBe Result.OK("123", "foo")
        digits("foo") shouldBe Result.Err("not a digit", "foo")
    }

    "seq" {
        val p = seq(::digits, stringLiteral("foo"))
        p("123foo") shouldBe Result.OK(Pair("123", "foo"), "")
    }

    "and" {
        val p = ::integer and string("foo")
        p("123foo") shouldBe Result.OK(Pair(123, Unit), "")
    }
})