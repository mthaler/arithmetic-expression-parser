package com.mthaler.aparser

/**
 * The Input class stores the text that should be parsed and the current position
 */
data class Input(val text: String, val position: Int) {

    val rest: String
        get() = text.substring(position)
}
