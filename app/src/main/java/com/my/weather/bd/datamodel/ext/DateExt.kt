package com.my.weather.bd.datamodel.ext

import com.my.weather.bd.data.Constants.SERVER_DATE_FORMAT
import com.my.weather.bd.data.Constants.SERVER_TIME_ONLY_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.getLogDateFormat(): String {
    val sdf = SimpleDateFormat(
        SERVER_DATE_FORMAT,
        Locale.ENGLISH
    )
    return sdf.format(this)
}

fun Date.getTimeFormat(): String {
    val sdf = SimpleDateFormat(
        SERVER_TIME_ONLY_FORMAT,
        Locale.ENGLISH
    )
    return sdf.format(this)
}