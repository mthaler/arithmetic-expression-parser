package com.mthaler

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class Eval : Grammar<Double>() {

    val num by regexToken("-?\\d+")
    val lpar by literalToken("(")
    val rpar by literalToken(")")
    val mul by literalToken("*")
    val pow by literalToken("^")
    val div by literalToken("/")
    val minus by literalToken("-")
    val plus by literalToken("+")
    val ws by regexToken("\\s+", ignore = true)

    val number by num use { text.toDouble() }

    val term: Parser<Double> by number or
            (skip(minus) and parser(this::term) map { -it }) or
            (skip(lpar) and parser(this::rootParser) and skip(rpar))

    override val rootParser: Parser<Double> by term
}