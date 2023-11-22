package com.mthaler.aparser.dice

import kotlin.random.Random

class DefaultRoll : Roll {
    private val random = Random.Default

    override fun d(sides: Int) = random.nextInt(sides) + 1
}