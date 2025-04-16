package com.bizsolutions.dealmate.ui.dashboard.charts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.bizsolutions.dealmate.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.time.format.DateTimeFormatter

class IncomeChartMarker(
    context: Context?, layoutResource: Int,
    private val list: List<MonthlyIncome>
) :
    MarkerView(context, layoutResource) {

    var drawingPosX = 0f
    var drawingPosY = 0f

    private var mOffset: MPPointF? = null

    private var amountTxt: TextView = findViewById(R.id.item_marker_txt)
    private var dateTxt: TextView = findViewById(R.id.item_marker_date)

    override fun refreshContent(entry: Entry, highlight: Highlight?) {
        val month = list[entry.x.toInt()].month
        val markerValue = entry.y.toInt()

        @SuppressLint("SetTextI18n")
        amountTxt.text = "%s USD".format(markerValue)
        dateTxt.text = month.format(DateTimeFormatter.ofPattern("LLLL yyyy"))

        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF? {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF(-width.toFloat(), -height.toFloat())
        }

        return mOffset
    }

    override fun draw(canvas: Canvas?, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)

        val offset = getOffsetForDrawingAtPoint(posX, posY)
        drawingPosX = posX + offset.x
        drawingPosY = posY + offset.y
    }
}