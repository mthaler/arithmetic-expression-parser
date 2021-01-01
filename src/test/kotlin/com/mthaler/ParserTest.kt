package com.mthaler

import com.mthaler.parser.Result
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ParserTest: StringSpec({

    "dot" {
        dot(".foo") shouldBe Result.OK(Unit, "foo")
        dot("foo") shouldBe Result.Err("a dot", "foo")
    }

    "integer" {
        integer("123foo") shouldBe Result.OK(123, "foo")
        integer("foo") shouldBe Result.Err("an integer", "foo")
    }

    "string" {
        val p = string("foo")
        p("foobar") shouldBe Result.OK(Unit, "bar")
        p("boofar") shouldBe Result.Err("'foo'", "boofar")
    }

    "seq" {
        val p = seq(::integer, string("foo"))
        p("123foo") shouldBe Result.OK(Pair(123, Unit), "")
    }

    "then" {
        val p = ::integer then string("foo")
        p("123foo") shouldBe Result.OK(Pair(123, Unit), "")
    }

    "choice" {
        val p = choice(string("foo").means(1), string("bar").means(2))
        p("foobar") shouldBe Result.OK(1, "bar")
        p("barfoo") shouldBe Result.OK(2, "foo")
        p("xyz") shouldBe Result.Err("'foo' or 'bar'", "xyz")
    }

    "or" {
        val p = string("foo").means(1) or string("bar").means(2)
        p("foobar") shouldBe Result.OK(1, "bar")
        p("barfoo") shouldBe Result.OK(2, "foo")
        p("xyz") shouldBe Result.Err("'foo' or 'bar'", "xyz")
    }

    "before" {
        val p = string("*").before(::integer)
        p("*123") shouldBe Result.OK(123, "")
    }

    "followedBy" {
        val p = ::integer.followedBy(string("%"))
        p("123%") shouldBe Result.OK(123, "")
    }

    "between" {
        val p = ::integer.between(string("<"), string(">"))
        p("<123>") shouldBe Result.OK(123, "")
    }
})