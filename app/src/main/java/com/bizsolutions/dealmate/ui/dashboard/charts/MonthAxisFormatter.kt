package com.bizsolutions.dealmate.ui.dashboard.charts

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.time.format.DateTimeFormatter

class MonthAxisFormatter(val list: List<MonthlyIncome>) :
    IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return if (value.toInt() in list.indices && value % 1 == 0f)
            list[value.toInt()].month.format(DateTimeFormatter.ofPattern("MMM yyyy"))
        else ""
    }
}