package com.mthaler.aparser.dice

interface RNG {

    fun nextInt(from: Int, until: Int)
}