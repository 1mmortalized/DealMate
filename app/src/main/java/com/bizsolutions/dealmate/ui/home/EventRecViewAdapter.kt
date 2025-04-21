package com.bizsolutions.dealmate.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.databinding.ItemEventBinding
import com.bizsolutions.dealmate.db.EventEntity
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class EventRecViewAdapter(
    private val onItemClicked: (EventEntity) -> Unit,
    private val onEditMenuItemClicked: (EventEntity) -> Unit,
    private val onDeleteMenuItemClicked: (EventEntity) -> Unit
) :
    ListAdapter<EventEntity, EventRecViewAdapter.EventViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding =
            ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        val binding = holder.binding

        binding.itemEventTitleTxt.text = event.title
        binding.itemEventTimeTxt.text = event.timeStart.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        )

        binding.root.setOnClickListener {
            onItemClicked(event)
        }

        if (event.completed) {
            binding.itemEventTitleTxt.paintFlags =
                binding.itemEventTitleTxt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.itemEventTimeTxt.paintFlags =
                binding.itemEventTimeTxt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            binding.itemEventTitleTxt.paintFlags =
                binding.itemEventTitleTxt.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            binding.itemEventTimeTxt.paintFlags =
                binding.itemEventTimeTxt.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }


    inner class EventViewHolder(val binding: ItemEventBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}