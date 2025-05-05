package com.bizsolutions.dealmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.ItemCallBinding
import com.bizsolutions.dealmate.db.CallWithClient
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class CallRecViewAdapter(
    private val onItemClicked: (Int) -> Unit
) :
    ListAdapter<CallWithClient, CallRecViewAdapter.CallViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CallViewHolder {
        val binding =
            ItemCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        val call = getItem(position)
        val binding = holder.binding

        binding.itemCallTitleTxt.text = call.call.title
        binding.itemCallClientTxt.text = call.client.name
        binding.itemCallTimeTxt.text = call.call.time.format(
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        )

        binding.itemCallIcon.setImageResource(
            if (call.call.completed)
                R.drawable.ic_check
            else R.drawable.ic_call)

        binding.root.setOnClickListener {
            onItemClicked(call.call.id)
        }
    }


    inner class CallViewHolder(val binding: ItemCallBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CallWithClient>() {
            override fun areItemsTheSame(oldItem: CallWithClient, newItem: CallWithClient): Boolean {
                return oldItem.call.id == newItem.call.id
            }

            override fun areContentsTheSame(oldItem: CallWithClient, newItem: CallWithClient): Boolean {
                return oldItem == newItem
            }
        }
    }
}