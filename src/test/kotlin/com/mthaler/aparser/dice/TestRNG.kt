package com.mthaler.aparser.dice

class TestRNG(val values: List<Int>): RNG {

    var count = 0

    override fun nextInt(from: Int, until: Int): Int {
        val value = values[count % values.size]
        count++
        if (value < from) {
            return from
        } else if(value >= until) {
            return until - 1
        } else {
            return value
        }
    }
}