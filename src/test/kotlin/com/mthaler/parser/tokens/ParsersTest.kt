package com.mthaler.parser.tokens

import com.mthaler.parser.Result
import com.mthaler.parser.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "charLiteral" {
        val p = charLiteral('.')
        p(".foo") shouldBe Result.OK(".", "foo")
        p("foo") shouldBe Result.Err("'.'", "foo")
    }

    "stringLiteral" {
        val p = stringLiteral("foo")
        p("foobar") shouldBe Result.OK("foo", "bar")
        p("boofar") shouldBe Result.Err("'foo'", "boofar")
    }

    "whitespaces" {
        whitespaces(" ") shouldBe Result.OK(" ", "")
        whitespaces(" 123") shouldBe Result.OK(" ", "123")
        whitespaces("  123") shouldBe Result.OK("  ", "123")
        whitespaces(" \t123") shouldBe Result.OK(" \t", "123")
        whitespaces("123") shouldBe Result.Err("whitespaces", "123")
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

    "identifier" {
        identifier("a") shouldBe Result.OK("a", "")
        identifier("_a") shouldBe Result.OK("_a", "")
        identifier("a_b") shouldBe Result.OK("a_b", "")
        identifier("a1") shouldBe Result.OK("a1", "")
        identifier("a123") shouldBe Result.OK("a123", "")
        identifier("a bcd") shouldBe Result.OK("a", " bcd")
    }

    "sequence" {
        val p = sequence(digits, stringLiteral("foo"))
        p("123foo") shouldBe Result.OK(Pair("123", "foo"), "")
        p("123") shouldBe Result.Err("'foo'", "")
        p("123bar") shouldBe Result.Err("'foo'", "bar")
    }

    "and" {
        val p = digits and stringLiteral("foo")
        p("123foo") shouldBe Result.OK(Pair("123", "foo"), "")
        p("123") shouldBe Result.Err("'foo'", "")
        p("123bar") shouldBe Result.Err("'foo'", "bar")
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
        p("foobar") shouldBe Result.OK("foo", "bar")
    }

    "zeroOrMore" {
        val ws = optional(whitespaces)
        val n = (ws and number and ws).map { it.middle() }
        val p = zeroOrMore(n)
        p("foo") shouldBe Result.OK(emptyList(), "foo")
        p("3.14") shouldBe Result.OK(listOf("3.14"), "")
        p(" 3.14 ") shouldBe Result.OK(listOf("3.14"), "")
        p(" 3.14 foo") shouldBe Result.OK(listOf("3.14"), "foo")
        p("3 4 5") shouldBe Result.OK(listOf("3", "4", "5"), "")
    }

    "oneOrMore" {
        val ws = optional(whitespaces)
        val n = (ws and number and ws).map { it.middle() }
        val p = oneOrMore(n)
        p("foo") shouldBe Result.Err("number", "foo")
        p("3.14") shouldBe Result.OK(listOf("3.14"), "")
        p(" 3.14 ") shouldBe Result.OK(listOf("3.14"), "")
        p(" 3.14 foo") shouldBe Result.OK(listOf("3.14"), "foo")
        p("3 4 5") shouldBe Result.OK(listOf("3", "4", "5"), "")
    }
})