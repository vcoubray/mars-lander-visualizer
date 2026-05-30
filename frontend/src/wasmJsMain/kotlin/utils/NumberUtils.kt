package utils

import kotlin.math.pow
import kotlin.math.round

fun Double.format(scale: Int = 2 ): String {
    val factor = 10.0.pow(scale)
    return (round(this * factor) / factor).toString()
}