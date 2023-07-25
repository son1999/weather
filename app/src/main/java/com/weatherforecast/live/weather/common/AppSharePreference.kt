package com.weatherforecast.live.weather.common

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppSharePreference @Inject constructor(private val context: Context) {
    companion object{
        const val APP_SHARE_KEY = "com.weatherforecast.live.weather"
        const val FIRST_LAUNCH = "com.weatherforecast.live.weather.first_launch"
    }

    fun getSharedPreferences(): SharedPreferences?{
        return context.getSharedPreferences(APP_SHARE_KEY,Context.MODE_PRIVATE)
    }

}