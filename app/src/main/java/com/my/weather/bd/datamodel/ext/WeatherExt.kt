package com.my.weather.bd.datamodel.ext

import com.my.weather.bd.data.Constants.KELVIN_CELSIUS_DIFF

fun Double.toCelsius(): Int {
    val celsius = this - KELVIN_CELSIUS_DIFF
    return celsius.toInt()
}
