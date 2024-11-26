package com.my.weather.bd.datamodel.models

import com.google.gson.annotations.SerializedName

data class WeatherErrorResponse(
    @SerializedName("cod") val cod: String,
    @SerializedName("message") val message: String
)