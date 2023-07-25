package com.weatherforecast.live.weather.data.services

import com.weatherforecast.live.weather.data.database.daos.CustomerDao
import javax.inject.Inject

class CustomerLocalService @Inject constructor(private val customerDao: CustomerDao) {
}