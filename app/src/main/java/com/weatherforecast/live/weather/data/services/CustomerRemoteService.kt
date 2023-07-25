package com.weatherforecast.live.weather.data.services

import com.weatherforecast.live.weather.data.apis.CustomerAPI
import javax.inject.Inject

class CustomerRemoteService @Inject constructor(private val customerAPI: CustomerAPI) {
}