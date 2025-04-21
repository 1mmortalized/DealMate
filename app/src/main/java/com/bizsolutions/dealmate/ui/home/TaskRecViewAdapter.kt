package com.bizsolutions.dealmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.ItemTaskBinding
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.db.TaskWithClient


class TaskRecViewAdapter(
    private val onItemClicked: (TaskWithClient) -> Unit,
    private val onEditMenuItemClicked: (TaskWithClient) -> Unit,
    private val onDeleteMenuItemClicked: (TaskWithClient) -> Unit
) :
    ListAdapter<TaskWithClient, TaskRecViewAdapter.TaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        val binding = holder.binding

        binding.itemTaskTitleTxt.text = task.task.title
        binding.itemTaskClientTxt.text = task.client.name
        binding.itemTaskPriorityTxt.setText(
            when(task.task.priority) {
                1 -> R.string.priority_high
                2 -> R.string.priority_medium
                else -> R.string.priority_low
            }
        )
        binding.itemTaskCheckbox.isChecked = task.task.completed

        binding.root.setOnClickListener {
            onItemClicked(task)
        }

//        val popupMenu = PopupMenu(context, holder.binding.itemBookQuoteMenuBtn)
//        popupMenu.menuInflater.inflate(R.menu.quote_item_menu, popupMenu.menu)
//        popupMenu.setupOptionalIcons(context)
//
//        popupMenu.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.quote_item_menu_item_edit -> onEditMenuItemClicked(quote)
//                R.id.quote_item_menu_item_copy -> {
//                    val clipboard: ClipboardManager? =
//                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//
//                    clipboard?.let {
//                        val clip = ClipData.newPlainText("quote_text_clip", quote.text)
//                        clipboard.setPrimaryClip(clip)
//                    }
//                }
//
//                R.id.quote_item_menu_item_select_text -> onSelectTextMenuItemClicked(quote.text)
//                R.id.quote_item_menu_item_delete -> onDeleteMenuItemClicked(quote)
//            }
//
//            true
//        }
//
//        holder.binding.itemBookQuoteMenuBtn.setOnClickListener {
//            popupMenu.show()
//        }


    }


    inner class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TaskWithClient>() {
            override fun areItemsTheSame(oldItem: TaskWithClient, newItem: TaskWithClient): Boolean {
                return oldItem.task.id == newItem.task.id
            }

            override fun areContentsTheSame(oldItem: TaskWithClient, newItem: TaskWithClient): Boolean {
                return oldItem == newItem
            }
        }
    }
}