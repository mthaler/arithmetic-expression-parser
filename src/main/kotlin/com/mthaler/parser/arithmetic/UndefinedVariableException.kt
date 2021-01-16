package com.mthaler.parser.arithmetic

class UndefinedVariableException(name: String) : Exception("Undefined variable: $name")