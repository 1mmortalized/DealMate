package com.bizsolutions.dealmate.ui.home.calendar

import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalendarViewModel : ViewModel() {
    private val baseDate = LocalDate.now()
    val baseIndex = Int.MAX_VALUE / 2

    fun getDateForPosition(position: Int): LocalDate {
        return baseDate.plusDays((position - baseIndex).toLong())
    }

    fun getPageForDate(date: LocalDate): Int {
        return baseIndex + ChronoUnit.DAYS.between(baseDate, date).toInt()
    }
}
