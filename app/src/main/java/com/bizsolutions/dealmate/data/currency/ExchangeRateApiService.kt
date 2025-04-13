package com.bizsolutions.dealmate.data.currency

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApiService {
    @GET("{apiKey}/latest/USD")
    suspend fun getRates(@Path("apiKey") apiKey: String): ExchangeRateResponse
}