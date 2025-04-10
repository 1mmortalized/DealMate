package com.bizsolutions.dealmate.ui.deals

import com.bizsolutions.dealmate.db.DealWithClient
import java.time.LocalDate

sealed class DealListItem {
    data class Header(val date: LocalDate) : DealListItem()
    data class Deal(val deal: DealWithClient) : DealListItem()
}