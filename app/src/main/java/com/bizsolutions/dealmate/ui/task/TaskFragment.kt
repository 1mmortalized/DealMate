package com.bizsolutions.dealmate.ui.task

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentTaskBinding
import com.bizsolutions.dealmate.ext.getThemeColor
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.ToolbarMenuHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.getValue

@AndroidEntryPoint
class TaskFragment : Fragment(), ToolbarMenuHandler {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private val args by navArgs<TaskFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        val colorPrimaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorPrimaryContainer)
        val colorSecondaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorSecondaryContainer)
        val colorTertiaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorTertiaryContainer)

        val colorOnPrimaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnPrimaryContainer)
        val colorOnSecondaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnSecondaryContainer)
        val colorOnTertiaryContainer = requireContext().getThemeColor(com.google.android.material.R.attr.colorOnTertiaryContainer)

        viewModel.getTask(args.taskId).observeOnce(viewLifecycleOwner) { taskWithClient ->
            val task = taskWithClient.task
            val client = taskWithClient.client

            binding.fragmentTaskTitleTxt.text = task.title

            binding.fragmentTaskPriorityChip.apply {
                when(task.priority) {
                    1 -> {
                        setText(R.string.priority_high)
                        chipBackgroundColor = ColorStateList.valueOf(colorPrimaryContainer)
                        setChipIconResource(R.drawable.ic_stat_3)

                        setTextColor(colorOnPrimaryContainer)
                        chipIconTint = ColorStateList.valueOf(colorOnPrimaryContainer)
                    }
                    2 -> {
                        setText(R.string.priority_medium)
                        chipBackgroundColor = ColorStateList.valueOf(colorSecondaryContainer)
                        setChipIconResource(R.drawable.ic_stat_2)

                        setTextColor(colorOnSecondaryContainer)
                        chipIconTint = ColorStateList.valueOf(colorOnSecondaryContainer)
                    }
                    else -> {
                        setText(R.string.priority_low)
                        chipBackgroundColor = ColorStateList.valueOf(colorTertiaryContainer)
                        setChipIconResource(R.drawable.ic_stat_1)

                        setTextColor(colorOnTertiaryContainer)
                        chipIconTint = ColorStateList.valueOf(colorOnTertiaryContainer)
                    }
                }
            }

            binding.fragmentTaskDateTxt.text = task.date.format(DateTimeFormatter.ofLocalizedDate(
                FormatStyle.MEDIUM))
            binding.fragmentTaskClientTxt.text = client.name

            binding.fragmentTaskProgressValueTxt.text = "%d%%".format(task.progress)
            binding.fragmentTaskProgressSlider.value = task.progress.toFloat()

            binding.fragmentTaskProgressSlider.addOnChangeListener { slider, value, fromUser ->
                binding.fragmentTaskProgressValueTxt.text = "%d%%".format(value.toInt())
                if (fromUser) viewModel.updateProgress(task.id, value.toInt())
            }

            binding.fragmentTaskDescriptionTxt.text = task.description
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
                Toast.makeText(requireContext(), "Edit task", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}