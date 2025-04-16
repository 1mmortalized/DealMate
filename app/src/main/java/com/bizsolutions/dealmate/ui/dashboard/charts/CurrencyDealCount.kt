package com.bizsolutions.dealmate.ui.dashboard.charts

import com.bizsolutions.dealmate.db.DealWithClient
import java.util.stream.Collectors.groupingBy

data class CurrencyDealCount(
    val currencyCode: String,
    val count: Int,
    val amount: Int
) {
    companion object {
        fun countDealsByCurrency(deals: List<DealWithClient>): List<CurrencyDealCount> {
            return deals
                .groupBy { it.deal.currency }
                .map { (currencyCode, dealsForCurrency) ->
                    val count = dealsForCurrency.size
                    val totalAmount = dealsForCurrency.sumOf { it.deal.amount }
                    CurrencyDealCount(currencyCode, count, totalAmount)
                }
                .sortedByDescending { it.count }
        }
    }
}