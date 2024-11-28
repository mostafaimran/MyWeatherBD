package com.my.weather.bd.datamodel.ext

import com.my.weather.bd.datamodel.exceptions.ClientRequestException
import com.my.weather.bd.datamodel.exceptions.HttpServerException
import com.my.weather.bd.datamodel.exceptions.LocalException
import com.my.weather.bd.datamodel.exceptions.NetworkException
import com.my.weather.bd.datamodel.exceptions.ServerException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

fun Exception.convertNetworkSpecificException(): Exception {
    return when {
        isServerError() -> {
            if (this is HttpException)
                HttpServerException(this.code())
            else
                ServerException(this.toString())
        }

        isClientRequestError() -> ClientRequestException(
            (this as HttpException).code(),
            this.message()
        )

        this is HttpException -> NetworkException(this)
        this is UnknownHostException -> NetworkException(this)
        this is ConnectException -> NetworkException(this)
        else -> LocalException(this.message ?: "Something went wrong, please try again later")
    }
}

fun Exception.isServerError(): Boolean {
    return if (this is HttpException && this.code() >= 500) return true else false
}

fun Exception.isClientRequestError(): Boolean {
    return if (this is HttpException && (this.code() in 400..499)) return true else false
}