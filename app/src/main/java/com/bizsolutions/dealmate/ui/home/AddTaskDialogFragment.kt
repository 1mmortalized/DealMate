package com.bizsolutions.dealmate.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDialogAddTaskBinding
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.bizsolutions.dealmate.ui.task.TaskPriority
import com.bizsolutions.dealmate.utils.showDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
open class AddTaskDialogFragment : FullscreenDialogFragment() {

    private var _binding: FragmentDialogAddTaskBinding? = null
    protected val binding get() = _binding!!

    protected var newTask: TaskEntity? = null

    protected val viewModel: HomeViewModel by viewModels()

    protected var pickedDate: LocalDate? = null

    private lateinit var clients: List<ClientEntity>

    @OptIn(ExperimentalTime::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _baseBinding = FragmentDialogBaseBinding.inflate(inflater, container, false)

        _binding =
            FragmentDialogAddTaskBinding.inflate(
                layoutInflater,
                baseBinding.root as ViewGroup,
                false
            )

        baseBinding.fragmentDialogBaseContainer.addView(binding.root)

        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.add_task_dialog_title)

        newTask = TaskEntity()

        baseBinding.fragmentDialogBaseSaveBtn.setOnClickListener {
            lifecycleScope.launch {
                newTask?.let { task ->
                    task.title = binding.fdAddTaskTitleEdt.editText!!.text.toString()
                    task.date = pickedDate!!
                    val priority = TaskPriority.fromLabel(
                        requireContext(),
                        binding.fdAddTaskPriorityEdtTxt.text.toString()
                    )

                    task.priority = if (priority != TaskPriority.AUTO)
                        priority.value
                    else {
                        viewModel.suggestTaskPriority(task.title).value
                    }

                    task.clientId = clients.find {
                        it.name == binding.fdAddTaskClientEdtTxt.text.toString()
                    }?.id ?: 0

                    task.description = binding.fdAddTaskDescriptionEdtTxt.text.toString()

                    onPositiveButtonClicked(task)
                }
            }

            dialog!!.dismiss()

            val snackbar = Snackbar.make(
                requireParentFragment().requireView(),
                R.string.task_saved_snack,
                Snackbar.LENGTH_SHORT
            )

            try {
                snackbar.setAnchorView(R.id.activity_main_bottom_nav_bar)
            }
            catch (_: IllegalArgumentException) {}

            snackbar.show()
        }

        baseBinding.fragmentDialogBaseCloseBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.fdAddTaskDateEdt.editText!!.setOnClickListener {
            showDatePicker(
                pickedDate,
                parentFragmentManager,
                "DATE_PICKER_TAG"
            ) { date ->
                pickedDate = date
                binding.fdAddTaskDateEdt.editText!!.setText(
                    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                )
            }
        }

        viewModel.allClients.observeOnce(viewLifecycleOwner) { clients ->
            (binding.fdAddTaskClientEdtTxt as MaterialAutoCompleteTextView)
                .setSimpleItems(clients.map { it.name }.toTypedArray())
            this.clients = clients
        }

        additionalOnCreateView()

        return baseBinding.root
    }

    protected open fun onPositiveButtonClicked(task: TaskEntity) {
        viewModel.addTask(task)
    }

    protected open fun additionalOnCreateView() {}
}

class EditTaskDialogFragment : AddTaskDialogFragment() {

    private val args by navArgs<EditTaskDialogFragmentArgs>()

    override fun additionalOnCreateView() {
        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.edit_task_dialog_title)

        val taskId = args.taskId
        viewModel.getTask(taskId).observeOnce(viewLifecycleOwner) { taskWithClient ->
            if (taskWithClient == null) return@observeOnce

            val task = taskWithClient.task
            val client = taskWithClient.client

            newTask?.id = task.id
            newTask?.progress = task.progress

            binding.fdAddTaskTitleEdt.editText!!.setText(task.title)

            pickedDate = task.date
            binding.fdAddTaskDateEdt.editText!!.setText(
                task.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            )

            binding.fdAddTaskPriorityEdtTxt.setText(
                getString(TaskPriority.fromValue(task.priority).labelResId), false
            )
            binding.fdAddTaskClientEdtTxt.setText(client.name, false)
            binding.fdAddTaskDescriptionEdtTxt.setText(task.description)
        }
    }

    override fun onPositiveButtonClicked(task: TaskEntity) {
        viewModel.updateTask(task)
    }
}