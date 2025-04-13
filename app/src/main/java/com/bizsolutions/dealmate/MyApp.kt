package com.bizsolutions.dealmate

import android.app.Application
import com.bizsolutions.dealmate.data.currency.CurrencyManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CurrencyManager.init(this)
    }
}