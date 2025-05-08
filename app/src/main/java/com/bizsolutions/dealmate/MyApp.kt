package com.bizsolutions.dealmate

import android.app.Application
import com.bizsolutions.dealmate.data.currency.CurrencyManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CurrencyManager.init(this)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(applicationContext))
        }
    }
}