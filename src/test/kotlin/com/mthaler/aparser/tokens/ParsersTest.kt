package com.mthaler.aparser.tokens

import com.mthaler.aparser.util.Result
import com.mthaler.aparser.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class ParsersTest: StringSpec({

    "charLiteral" {
        val p = charLiteral('.')
        p(".foo") shouldBe Result.OK(".".toBuffer(), "foo")
        p("foo") shouldBe Result.Err("'.'", "foo")
    }

    "stringLiteral" {
        val p = stringLiteral("foo")
        p("foobar") shouldBe Result.OK("foo".toBuffer(), "bar")
        p("boofar") shouldBe Result.Err("'foo'", "boofar")
    }

    "whitespaces" {
        whitespaces(" ") shouldBe Result.OK(" ".toBuffer(), "")
        whitespaces(" 123") shouldBe Result.OK(" ".toBuffer(), "123")
        whitespaces("  123") shouldBe Result.OK("  ".toBuffer(), "123")
        whitespaces(" \t123") shouldBe Result.OK(" \t".toBuffer(), "123")
        whitespaces("123") shouldBe Result.Err("whitespaces", "123")
    }

    "lettersOrDigits" {
        lettersOrDigits("a") shouldBe Result.OK("a".toBuffer(), "")
        lettersOrDigits("ab") shouldBe Result.OK("ab".toBuffer(), "")
        lettersOrDigits("abc") shouldBe Result.OK("abc".toBuffer(), "")
        lettersOrDigits("1") shouldBe Result.OK("1".toBuffer(), "")
        lettersOrDigits("12") shouldBe Result.OK("12".toBuffer(), "")
        lettersOrDigits("123") shouldBe Result.OK("123".toBuffer(), "")
        lettersOrDigits("123foo") shouldBe Result.OK("123foo".toBuffer(), "")
        lettersOrDigits("foo") shouldBe Result.OK("foo".toBuffer(), "")
        lettersOrDigits("a*") shouldBe Result.OK("a".toBuffer(), "*")
        lettersOrDigits("*") shouldBe Result.Err("letters or digits", "*")
    }

    "digits" {
        digits("1") shouldBe Result.OK("1".toBuffer(), "")
        digits("12") shouldBe Result.OK("12".toBuffer(), "")
        digits("123") shouldBe Result.OK("123".toBuffer(), "")
        digits("123foo") shouldBe Result.OK("123".toBuffer(), "foo")
        digits("foo") shouldBe Result.Err("digits", "foo")
    }

    "lowerCaseLetters" {
        lowerCaseLetters("") shouldBe Result.Err("lower case letters", "")
        lowerCaseLetters("a") shouldBe Result.OK("a".toBuffer(), "")
        lowerCaseLetters("A") shouldBe Result.Err("lower case letters", "A")
        lowerCaseLetters("1") shouldBe Result.Err("lower case letters", "1")
        lowerCaseLetters("abc") shouldBe Result.OK("abc".toBuffer(), "")
        lowerCaseLetters("abC") shouldBe Result.OK("ab".toBuffer(), "C")
        lowerCaseLetters("aBc") shouldBe Result.OK("a".toBuffer(), "Bc")
        lowerCaseLetters("ab1") shouldBe Result.OK("ab".toBuffer(), "1")
        lowerCaseLetters("a1c") shouldBe Result.OK("a".toBuffer(), "1c")
    }

    "upperCaseLetters" {
        upperCaseLetters("") shouldBe Result.Err("upper case letters", "")
        upperCaseLetters("A") shouldBe Result.OK("A".toBuffer(), "")
        upperCaseLetters("a") shouldBe Result.Err("upper case letters", "a")
        upperCaseLetters("1") shouldBe Result.Err("upper case letters", "1")
        upperCaseLetters("ABC") shouldBe Result.OK("ABC".toBuffer(), "")
        upperCaseLetters("ABc") shouldBe Result.OK("AB".toBuffer(), "c")
        upperCaseLetters("AbC") shouldBe Result.OK("A".toBuffer(), "bC")
        upperCaseLetters("AB1") shouldBe Result.OK("AB".toBuffer(), "1")
        upperCaseLetters("A1C") shouldBe Result.OK("A".toBuffer(), "1C")
    }

    "number" {
        number("123") shouldBe Result.OK("123".toBuffer(), "")
        number("123foo") shouldBe Result.OK("123".toBuffer(), "foo")
        number("3.14") shouldBe Result.OK("3.14".toBuffer(), "")
        number("3.14foo") shouldBe Result.OK("3.14".toBuffer(), "foo")
        number("foo") shouldBe Result.Err("number", "foo")
        number("foo123") shouldBe Result.Err("number", "foo123")
        number("10e3") shouldBe Result.OK("10e3".toBuffer(), "")
        number("10E3") shouldBe Result.OK("10E3".toBuffer(), "")
    }

    "identifier" {
        identifier("a") shouldBe Result.OK("a".toBuffer(), "")
        identifier("_a") shouldBe Result.OK("_a".toBuffer(), "")
        identifier("a_b") shouldBe Result.OK("a_b".toBuffer(), "")
        identifier("a1") shouldBe Result.OK("a1".toBuffer(), "")
        identifier("a123") shouldBe Result.OK("a123".toBuffer(), "")
        identifier("a bcd") shouldBe Result.OK("a".toBuffer(), " bcd")
        identifier("A") shouldBe Result.OK("A".toBuffer(), "")
        identifier("_A") shouldBe Result.OK("_A".toBuffer(), "")
        identifier("A_B") shouldBe Result.OK("A_B".toBuffer(), "")
        identifier("A1") shouldBe Result.OK("A1".toBuffer(), "")
        identifier("A BCD") shouldBe Result.OK("A".toBuffer(), " BCD")
    }

    "sequence" {
        val p = sequence(digits, stringLiteral("foo"))
        p("123foo") shouldBe Result.OK(Pair("123".toBuffer(), "foo".toBuffer()), "")
        p("123") shouldBe Result.Err("'foo'", "")
        p("123bar") shouldBe Result.Err("'foo'", "bar")
    }

    "and" {
        val p = digits and stringLiteral("foo")
        p("123foo") shouldBe Result.OK(Pair("123".toBuffer(), "foo".toBuffer()), "")
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
        p("foo") shouldBe Result.OK("foo".toBuffer(), "")
        p("bar") shouldBe Result.OK(null, "bar")
        p("foobar") shouldBe Result.OK("foo".toBuffer(), "bar")
    }

//    "zeroOrMore" {
//        val ws = optional(whitespaces)
//        val n = (ws and number and ws).map { it.middle() }
//        val p = zeroOrMore(n)
//        p("foo") shouldBe Result.OK(emptyList(), "foo")
//        p("3.14") shouldBe Result.OK(listOf("3.14"), "")
//        p(" 3.14 ") shouldBe Result.OK(listOf("3.14"), "")
//        p(" 3.14 foo") shouldBe Result.OK(listOf("3.14"), "foo")
//        p("3 4 5") shouldBe Result.OK(listOf("3", "4", "5"), "")
//    }
//
//    "oneOrMore" {
//        val ws = optional(whitespaces)
//        val n = (ws and number and ws).map { it.middle() }
//        val p = oneOrMore(n)
//        p("foo") shouldBe Result.Err("number", "foo")
//        p("3.14") shouldBe Result.OK(listOf("3.14"), "")
//        p(" 3.14 ") shouldBe Result.OK(listOf("3.14"), "")
//        p(" 3.14 foo") shouldBe Result.OK(listOf("3.14"), "foo")
//        p("3 4 5") shouldBe Result.OK(listOf("3", "4", "5"), "")
//    }
})