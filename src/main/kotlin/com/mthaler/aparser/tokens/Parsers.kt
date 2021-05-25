package com.mthaler.aparser.tokens

import com.mthaler.aparser.Parser
import com.mthaler.aparser.util.Result

fun interface TokenParser: Parser<String>

val numberRegex = Regex("^\\d+(\\.\\d*)?([eE][+-]?\\d+)?")

fun charLiteral(c: Char) = TokenParser { input ->
    if (input.startsWith(c))
        Result.OK(c.toString(), input.substring(1))
    else
        Result.Err("'$c'", input)
}

fun stringLiteral(s: String) = TokenParser { input ->
    if (input.startsWith(s))
        Result.OK(s, input.substring(s.length))
    else
        Result.Err("'$s'", input)
}

val whitespaces = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("whitespaces", input)
        } else if (!input[0].isWhitespace()) {
            return Result.Err("whitespaces", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isWhitespace())
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}

val lettersOrDigits = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("letters or digits", input)
        } else if (!input[0].isLetterOrDigit()) {
            return Result.Err("letters or digits", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isLetterOrDigit())
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}

val digits = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("digits", input)
        } else if (!input[0].isDigit()) {
            return Result.Err("digits", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isDigit())
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}

val lowerCaseLetters = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("lower case letters", input)
        } else if (!input[0].isLowerCase()) {
            return Result.Err("lower case letters", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isLowerCase())
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}

val upperCaseLetters = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("upper case letters", input)
        } else if (!input[0].isUpperCase()) {
            return Result.Err("upper case letters", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isUpperCase())
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}

val number = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("number", input)
        } else {
            val m = numberRegex.find(input)
            if (m != null) {
                return Result.OK(m.value, input.substring(m.range.endInclusive + 1))
            } else {
                return Result.Err("number", input)
            }
        }
    }
}

val identifier = object : TokenParser {

    override fun parse(input: String): Result<String> {
        if (input.isEmpty()) {
            return Result.Err("identifier", input)
        } else if (!(input[0].isLetter() || input[0] == '_')) {
            return Result.Err("identifier", input)
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length) {
                val c = input[i]
                if (c.isLetterOrDigit() || c == '_')
                    sb.append(c)
                else
                    return Result.OK(sb.toString(), input.substring(i))
            }
            return Result.OK(sb.toString(), "")
        }
    }
}