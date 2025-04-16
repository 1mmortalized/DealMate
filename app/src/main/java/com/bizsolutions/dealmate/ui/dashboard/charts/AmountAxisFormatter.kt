package com.bizsolutions.dealmate.ui.dashboard.charts

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.Locale
import kotlin.math.roundToInt

class AmountAxisFormatter() :
    IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?) =
        formatCompactNumber(value.roundToInt())

    private fun formatCompactNumber(number: Int): String {
        val absNumber = kotlin.math.abs(number)
        val suffix = when {
            absNumber >= 1_000_000_000 -> "B"
            absNumber >= 1_000_000 -> "M"
            absNumber >= 1_000 -> "k"
            else -> ""
        }

        val value = when {
            absNumber >= 1_000_000_000 -> number / 1_000_000_000.0
            absNumber >= 1_000_000 -> number / 1_000_000.0
            absNumber >= 1_000 -> number / 1_000.0
            else -> number.toFloat()
        }

        return if (suffix.isEmpty()) {
            number.toString()
        } else {
            String.format(Locale.getDefault(), "%.1f%s", value, suffix).replace(".0", "")
                .replace(",0", "")
        }
    }
}