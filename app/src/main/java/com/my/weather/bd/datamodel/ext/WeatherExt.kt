package com.my.weather.bd.datamodel.ext

import com.my.weather.bd.data.Constants.KELVIN_CELSIUS_DIFF
import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.toCelsius(): Double {
    val celsius = this - KELVIN_CELSIUS_DIFF;
    return celsius.roundOffDecimal()
}

fun Double.roundOffDecimal(): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(this).toDouble()
}