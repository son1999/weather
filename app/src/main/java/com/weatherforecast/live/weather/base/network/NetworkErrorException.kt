package com.weatherforecast.live.weather.base.network

public open class NetworkErrorException (val responseMessage: String? = null): Exception() {
}