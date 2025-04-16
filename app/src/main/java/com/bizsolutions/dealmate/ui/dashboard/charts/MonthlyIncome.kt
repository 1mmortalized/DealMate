package com.bizsolutions.dealmate.ui.dashboard.charts

import com.bizsolutions.dealmate.db.DealWithClient
import java.time.YearMonth
import kotlin.collections.component1
import kotlin.collections.component2

data class MonthlyIncome(
    val month: YearMonth,
    val total: Int
) {
    companion object {
        fun getMonthlyIncome(deals: List<DealWithClient>): List<MonthlyIncome> {
            return deals
                .groupBy { YearMonth.from(it.deal.date) }
                .map { (month, dealsInMonth) ->
                    MonthlyIncome(
                        month = month,
                        total = dealsInMonth.sumOf { it.deal.amount }
                    )
                }
                .sortedBy { it.month }
        }
    }
}