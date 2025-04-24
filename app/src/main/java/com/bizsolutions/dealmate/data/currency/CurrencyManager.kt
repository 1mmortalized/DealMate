package com.bizsolutions.dealmate.data.currency

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bizsolutions.dealmate.ext.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyManager {
    private const val TAG = "CurrencyManager"
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/"
    private const val API_KEY = "2d830d478520c737f2dc2cec"
    private const val PREF_EXCHANGE_RATES = "exchange_rates"
    private const val PREF_LAST_UPDATE = "last_update"

    var currencyRates: Map<String, Double>? = null

    fun init(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            currencyRates = getRates(context)
        }
    }

    private suspend fun getRates(context: Context): Map<String, Double>? {
        val prefs = context.dataStore.data.first()
        val jsonString = prefs[stringPreferencesKey(PREF_EXCHANGE_RATES)]
        val lastUpdate = prefs[longPreferencesKey(PREF_LAST_UPDATE)] ?: 0L
        val isOutdated = System.currentTimeMillis() - lastUpdate > 24 * 60 * 60 * 1000

        return if (jsonString == null || (isOutdated && isOnline(context))) {
            fetchRatesFromApi(context)
        } else {
            try {
                val response = Gson().fromJson(jsonString, ExchangeRateResponse::class.java)
                response.conversionRates
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse cached rates: ${e.message}")
                fetchRatesFromApi(context)
            }
        }
    }

    private suspend fun fetchRatesFromApi(context: Context): Map<String, Double>? {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ExchangeRateApiService::class.java)
            val response = service.getRates(API_KEY)

            if (response.result == "success") {
                val jsonString = Gson().toJson(response)

                context.dataStore.edit { prefs ->
                    prefs[stringPreferencesKey(PREF_EXCHANGE_RATES)] = jsonString
                    prefs[longPreferencesKey(PREF_LAST_UPDATE)] = System.currentTimeMillis()
                }

                Log.d(TAG, "Currency rates loaded successfully.")
                response.conversionRates
            } else {
                Log.e(TAG, "Currency API returned failure result.")
                null
            }
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.code()} ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch currency rates: ${e.message}")
            null
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }

        return false
    }
}