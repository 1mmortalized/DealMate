package com.bizsolutions.dealmate.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.ItemTaskBinding
import com.bizsolutions.dealmate.db.TaskWithClient
import com.bizsolutions.dealmate.ui.task.TaskPriority
import com.google.android.material.checkbox.MaterialCheckBox


class TaskRecViewAdapter(
    private val onItemClicked: (TaskWithClient) -> Unit,
    private val onItemCheckedStateChanged: (TaskWithClient, Boolean) -> Unit,
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

        val priority = TaskPriority.fromValue(task.task.priority)
        binding.itemTaskPriorityTxt.setText(priority.labelResId)

        binding.itemTaskPriorityIcon.setImageResource(
            when(TaskPriority.fromValue(task.task.priority)) {
                TaskPriority.HIGH -> R.drawable.ic_stat_3_20
                TaskPriority.MEDIUM -> R.drawable.ic_stat_2_20
                else -> R.drawable.ic_stat_1_20
            }
        )

        binding.itemTaskCheckbox.setOnCheckedChangeListener(null)
        binding.itemTaskCheckbox.checkedState =
            when (task.task.progress) {
                0 -> MaterialCheckBox.STATE_UNCHECKED
                100 -> MaterialCheckBox.STATE_CHECKED
                else -> MaterialCheckBox.STATE_INDETERMINATE
            }

        binding.itemTaskCheckbox.setOnCheckedChangeListener { _, isChecked ->
            onItemCheckedStateChanged(task, isChecked)
        }

        binding.root.setOnClickListener {
            onItemClicked(task)
        }
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