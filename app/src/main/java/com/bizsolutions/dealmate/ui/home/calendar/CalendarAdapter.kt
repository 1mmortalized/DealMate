package com.bizsolutions.dealmate.ui.home.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bizsolutions.dealmate.R
import com.google.android.material.card.MaterialCardView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarAdapter(
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
        val context = holder.itemView.context

        holder.textView.text = date.format(DateTimeFormatter.ofPattern("d"))

        when (position) {
            selectedDatePosition -> holder.card.setCardBackgroundColor(context.getColor(R.color.purple_200))
            viewModel.baseIndex -> holder.card.setCardBackgroundColor(context.getColor(R.color.teal_200))
            else -> holder.card.setCardBackgroundColor(Color.TRANSPARENT)
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
        val textView: TextView = view.findViewById(R.id.item_calendar_txt)
    }
}

