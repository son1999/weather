package com.weatherforecast.live.weather.common

import android.app.Activity
import com.weatherforecast.live.weather.CustomApplication

val Activity.customApplication: CustomApplication
get() = application as CustomApplication