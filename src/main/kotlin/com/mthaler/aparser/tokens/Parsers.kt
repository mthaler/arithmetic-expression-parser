package com.mthaler.aparser.tokens

import com.mthaler.aparser.Buffer
import com.mthaler.aparser.Parser
import com.mthaler.aparser.util.Result
import java.util.BitSet

fun interface TokenParser: Parser<Buffer>

val numberRegex = Regex("^\\d+(\\.\\d*)?([eE][+-]?\\d+)?")

fun charLiteral(c: Char) = Parser<Buffer> { input ->
    if (input.startsWith(c))
        Result.OK(Buffer(c.toString()), input.substring(1))
    else
        Result.Err("'$c'", input.text())
}

fun stringLiteral(s: String) = Parser<Buffer> { input ->
    if (input.startsWith(s))
        Result.OK(Buffer(s), input.substring(s.length))
    else
        Result.Err("'$s'", input.text())
}

val whitespaces = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("whitespaces", input.text())
        } else if (!input[0].isWhitespace()) {
            return Result.Err("whitespaces", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isWhitespace())
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}

val lettersOrDigits = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("letters or digits", input.text())
        } else if (!input[0].isLetterOrDigit()) {
            return Result.Err("letters or digits", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isLetterOrDigit())
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}

val digits = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("digits", input.text())
        } else if (!input[0].isDigit()) {
            return Result.Err("digits", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isDigit())
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}

val lowerCaseLetters = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("lower case letters", input.text())
        } else if (!input[0].isLowerCase()) {
            return Result.Err("lower case letters", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isLowerCase())
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}

val upperCaseLetters = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("upper case letters", input.text())
        } else if (!input[0].isUpperCase()) {
            return Result.Err("upper case letters", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isUpperCase())
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}

val number = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("number", input.text())
        } else {
            val m = numberRegex.find(input.text())
            if (m != null) {
                return Result.OK(Buffer(m.value), input.substring(m.range.endInclusive + 1))
            } else {
                return Result.Err("number", input.text())
            }
        }
    }
}

val identifier = object : TokenParser {

    override fun parse(input: Buffer): Result<Buffer> {
        if (input.isEmpty()) {
            return Result.Err("identifier", input.text())
        } else if (!(input[0].isLetter() || input[0] == '_')) {
            return Result.Err("identifier", input.text())
        } else {
            val sb = StringBuffer()
            sb.append(input[0])
            for (i in 1 until input.length()) {
                val c = input[i]
                if (c.isLetterOrDigit() || c == '_')
                    sb.append(c)
                else
                    return Result.OK(Buffer(sb.toString()), input.substring(i))
            }
            return Result.OK(Buffer(sb.toString()), "")
        }
    }
}