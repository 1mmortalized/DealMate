package com.bizsolutions.dealmate.ui.home.calendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bizsolutions.dealmate.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    context: Context,
    private val viewModel: CalendarViewModel,
    private val onDateSelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarItemViewHolder>() {
    private var selectedDatePosition: Int = viewModel.baseIndex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        val date = viewModel.getDateForPosition(position)

        holder.dayTxt.text = date.format(DateTimeFormatter.ofPattern("d"))
        holder.weekdayTxt.text = date.format(DateTimeFormatter.ofPattern("E")).uppercase()

        when (position) {
            selectedDatePosition -> {
                holder.card.setCardBackgroundColor(colorPrimary)
                holder.dayTxt.setTextColor(colorOnPrimary)
            }
            viewModel.baseIndex -> {
                holder.card.setCardBackgroundColor(colorSecondaryContainer)
                holder.dayTxt.setTextColor(colorOnSecondaryContainer)
            }
            else -> {
                holder.card.setCardBackgroundColor(Color.TRANSPARENT)
                holder.dayTxt.setTextColor(colorOnSurface)
            }
        }

        holder.itemView.setOnClickListener {
            selectItem(position)
            onDateSelected(date)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    fun selectItem(newPosition: Int) {
        val previousPosition = selectedDatePosition
        selectedDatePosition = newPosition

        notifyItemChanged(previousPosition)
        notifyItemChanged(newPosition)
    }

    inner class CalendarItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.item_calendar_card)
        val dayTxt: TextView = view.findViewById(R.id.item_calendar_day_txt)
        val weekdayTxt: TextView = view.findViewById(R.id.item_calendar_weekday_txt)
    }

    private val colorPrimary: Int = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorPrimary,
        Color.BLACK
    )

    private val colorSecondaryContainer = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorSecondaryContainer,
        Color.BLACK
    )

    private val colorOnPrimary: Int = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorOnPrimary,
        Color.BLACK
    )

    private val colorOnSecondaryContainer = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorOnSecondaryContainer,
        Color.BLACK
    )

    private val colorOnSurface = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorOnSurface,
        Color.BLACK
    )
}

