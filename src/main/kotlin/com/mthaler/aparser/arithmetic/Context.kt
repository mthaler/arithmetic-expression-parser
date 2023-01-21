package com.mthaler.aparser.arithmetic

/**
 * Context for evaluating expressions
 */
data class Context(val trigonometricUnit: TrigonometricUnit, val globalVars: Map<String, Double>) {

    companion object {
        val Empty = Context(TrigonometricUnit.Rad, emptyMap())
    }
}
