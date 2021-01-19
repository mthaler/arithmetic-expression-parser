package com.mthaler.aparser.arithmetic

data class Context(val globalVars: Map<String, Double>) {

    companion object {
        val Empty = Context(emptyMap())
    }
}