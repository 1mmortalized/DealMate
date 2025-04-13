package com.bizsolutions.dealmate.data.currency

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>
)
