package com.my.weather.bd.core

interface JsonConverter {
    fun <T> getJsonString(jsonObject: T): String
    fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T?
    fun <T> fromJsontoArrayList(jsonString: String?, classOfT: Class<T>): ArrayList<T>?
    fun <T, R> fromJsontoHashmap(jsonString: String?): HashMap<T, R>?
    fun <T, R> fromJson(jsonString: String?, classOfT1: Class<T>, classOfT2: Class<R>): T?
}
