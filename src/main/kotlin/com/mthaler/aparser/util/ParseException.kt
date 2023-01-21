package com.mthaler.aparser.util

data class ParseException(val expected: String, val input: String): Exception()