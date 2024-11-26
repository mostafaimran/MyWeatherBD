package com.my.weather.bd.core

import com.google.gson.annotations.SerializedName
import com.my.weather.bd.core.base.BaseApiResponse


open class ApiResponse<T>() : BaseApiResponse() {
    @SerializedName("data")
    var data: T? = null
}