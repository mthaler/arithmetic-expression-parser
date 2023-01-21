package com.mthaler.aparser

class Buffer(val text: String)

fun String.toBuffer(): Buffer = Buffer(this)