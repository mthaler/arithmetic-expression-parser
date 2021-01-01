package com.mthaler.parser

import com.mthaler.Result
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
        whitespace("123") shouldBe Result.Err("whitespaces", "123")
    }

    "digits" {
        digits("1") shouldBe Result.OK("1", "")
        digits("12") shouldBe Result.OK("12", "")
        digits("123") shouldBe Result.OK("123", "")
        digits("123foo") shouldBe Result.OK("123", "foo")
        digits("foo") shouldBe Result.Err("digits", "foo")
    }

    "number" {
        number("123") shouldBe Result.OK("123", "")
        number("123foo") shouldBe Result.OK("123", "foo")
        number("3.14") shouldBe Result.OK("3.14", "")
        number("3.14foo") shouldBe Result.OK("3.14", "foo")
        number("foo") shouldBe Result.Err("number", "foo")
        number("foo123") shouldBe Result.Err("number", "foo123")

    }

    "sequence" {
        val p = sequence(::digits, stringLiteral("foo"))
        p("123foo") shouldBe Result.OK(Pair("123", "foo"), "")
    }

    "and" {
        val p = ::digits and stringLiteral("foo")
        p("123foo") shouldBe Result.OK(Pair("123", "foo"), "")
    }

    "orderedChoice" {
        val p = orderedChoice(stringLiteral("foo").means(1), stringLiteral("bar").means(2))
        p("foobar") shouldBe Result.OK(1, "bar")
        p("barfoo") shouldBe Result.OK(2, "foo")
        p("xyz") shouldBe Result.Err("'foo' or 'bar'", "xyz")
    }

    "or" {
        val p = stringLiteral("foo").means(1) or stringLiteral("bar").means(2)
        p("foobar") shouldBe Result.OK(1, "bar")
        p("barfoo") shouldBe Result.OK(2, "foo")
        p("xyz") shouldBe Result.Err("'foo' or 'bar'", "xyz")
    }

    "optional" {
        val p = optional(stringLiteral("foo"))
        p("foo") shouldBe Result.OK("foo", "")
        p("bar") shouldBe Result.OK(null, "bar")
    }
})