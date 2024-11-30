package com.my.weather.bd.coreandroid

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.my.weather.bd.core.JsonConverter

class GsonConverter(private val gson: Gson) : JsonConverter {

    override fun <T> getJsonString(jsonObject: T): String {
        return gson.toJson(jsonObject)
    }

    override fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T? {
        if (jsonString.isNullOrBlank()) return null
        return gson.fromJson(jsonString, classOfT)
    }

    override fun <T> fromJsontoArrayList(jsonString: String?, classOfT: Class<T>): ArrayList<T>? {
        if (jsonString.isNullOrBlank()) return null
        return gson.fromJson(
            jsonString,
            TypeToken.getParameterized(ArrayList::class.java, classOfT).type
        )
    }

    override fun <T, R> fromJsontoHashmap(jsonString: String?): HashMap<T, R>? {
        if (jsonString.isNullOrBlank()) return null
        return gson.fromJson(jsonString, object : TypeToken<HashMap<T, R>?>() {}.type)
    }

    override fun <T, R> fromJson(
        jsonString: String?,
        classOfT1: Class<T>,
        classOfT2: Class<R>
    ): T? {
        if (jsonString.isNullOrBlank()) return null
        return gson.fromJson(
            jsonString,
            TypeToken.getParameterized(classOfT1, classOfT2).type
        )
    }

}