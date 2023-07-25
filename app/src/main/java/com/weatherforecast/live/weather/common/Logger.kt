package com.weatherforecast.live.weather.common

import android.util.Log
import com.weatherforecast.live.weather.BuildConfig

object Logger {

    fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

}