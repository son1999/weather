package com.weatherforecast.live.weather.data.repositories

import com.weatherforecast.live.weather.data.services.CustomerLocalService
import com.weatherforecast.live.weather.data.services.CustomerRemoteService
import com.weatherforecast.live.weather.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher

class CustomerRepository constructor(
    private val customerRemoteService: CustomerRemoteService,
    private val customerLocalService: CustomerLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
}