package com.mthaler.aparser.util

import java.math.BigDecimal
import java.math.RoundingMode

fun roundDouble(d: Double, places: Int): Double {
    var bigDecimal = BigDecimal(java.lang.Double.toString(d))
    bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP)
    return bigDecimal.toDouble()
}