package com.weatherforecast.live.weather.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.weatherforecast.live.weather.data.database.entities.CustomerEntity

@Dao
interface CustomerDao {

    @Query("select * from customer")
    fun getAll(): List<CustomerEntity>

}