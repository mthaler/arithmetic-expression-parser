package com.mthaler.aparser.dice

import kotlin.random.Random

interface RNG {

    fun nextInt(from: Int, until: Int): Int

    companion object {

        val Default = object : RNG {
            val random = Random.Default

            override fun nextInt(from: Int, until: Int): Int = random.nextInt(from, until)
        }
    }
}