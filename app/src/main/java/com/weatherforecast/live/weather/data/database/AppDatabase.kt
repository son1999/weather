package com.weatherforecast.live.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weatherforecast.live.weather.data.database.daos.CustomerDao
import com.weatherforecast.live.weather.data.database.entities.CustomerEntity

@Database(entities = [CustomerEntity::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun customerDao(): CustomerDao
}