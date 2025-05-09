package com.bizsolutions.dealmate.ui.task

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentTaskBinding
import com.bizsolutions.dealmate.db.TaskWithClient
import com.bizsolutions.dealmate.ext.getThemeColor
import com.bizsolutions.dealmate.ext.safeNavigate
import com.bizsolutions.dealmate.ext.showDeleteConfirmationDialog
import com.bizsolutions.dealmate.ui.ToolbarMenuHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class TaskFragment : Fragment(), ToolbarMenuHandler {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private val args by navArgs<TaskFragmentArgs>()

    private var lastTaskWithClient: TaskWithClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        viewModel.getTask(args.taskId).observe(viewLifecycleOwner) { newTaskWithClient ->
            if (newTaskWithClient == null) return@observe

            val oldTaskWithClient = lastTaskWithClient
            val newTask = newTaskWithClient.task
            val newClient = newTaskWithClient.client

            if (oldTaskWithClient != null) {
                val oldTask = oldTaskWithClient.task
                val oldClient = oldTaskWithClient.client

                val isSame = oldTask.title == newTask.title &&
                        oldTask.priority == newTask.priority &&
                        oldTask.date == newTask.date &&
                        oldTask.description == newTask.description &&
                        oldClient.name == newClient.name

                if (isSame) {
                    return@observe
                }
            }

            lastTaskWithClient = newTaskWithClient

            binding.fragmentTaskTitleTxt.text = newTask.title

            val priority = TaskPriority.fromValue(newTask.priority)
            val containerColor = requireContext().getThemeColor(priority.containerColor)
            val onContainerColor = requireContext().getThemeColor(priority.onContainerColor)

            binding.fragmentTaskPriorityChip.apply {
                setText(priority.labelResId)
                setChipIconResource(priority.iconResId)
                chipBackgroundColor = ColorStateList.valueOf(containerColor)
                setTextColor(onContainerColor)
                chipIconTint = ColorStateList.valueOf(onContainerColor)
            }

            binding.fragmentTaskDateTxt.text = newTask.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            binding.fragmentTaskClientTxt.text = newClient.name
            binding.fragmentTaskDescriptionTxt.text = newTask.description

            binding.fragmentTaskProgressValueTxt.text = "%d%%".format(newTask.progress)
            binding.fragmentTaskProgressSlider.value = newTask.progress.toFloat()

            binding.fragmentTaskProgressSlider.clearOnChangeListeners()
            binding.fragmentTaskProgressSlider.addOnChangeListener { _, value, fromUser ->
                binding.fragmentTaskProgressValueTxt.text = "%d%%".format(value.toInt())
                if (fromUser) viewModel.updateProgress(
                    newTask.copy(
                        progress = value.toInt()
                    )
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onToolbarMenuItemClicked(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                val taskId = args.taskId

                val directions = TaskFragmentDirections.actionTaskToEditTask(taskId)
                findNavController().safeNavigate(directions)

                true
            }
            R.id.action_delete -> {
                requireContext().showDeleteConfirmationDialog(
                    R.string.del_task_dlg_title,
                    R.string.task_removed_snack,
                    requireView()
                ) {
                    findNavController().popBackStack()
                    viewModel.removeTask(args.taskId)
                }

                true
            }
            else -> false
        }
    }
}