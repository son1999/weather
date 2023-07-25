package com.weatherforecast.live.weather.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity (@PrimaryKey val id: Int)