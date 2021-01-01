package com.mthaler.parser

fun <A, B, C> Pair<Pair<A, B>, C>.middle(): B = this.first.second