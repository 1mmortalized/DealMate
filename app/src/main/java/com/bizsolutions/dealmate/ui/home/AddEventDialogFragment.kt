package com.bizsolutions.dealmate.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDialogAddEventBinding
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.bizsolutions.dealmate.utils.showTimePicker
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import kotlin.getValue
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
open class AddEventDialogFragment : FullscreenDialogFragment() {

    private var _binding: FragmentDialogAddEventBinding? = null
    protected val binding get() = _binding!!

    protected var newEvent: EventEntity? = null

    protected val viewModel: HomeViewModel by viewModels()

    protected var pickedStartTime: LocalTime? = null
    protected var pickedEndTime: LocalTime? = null
    protected var pickedDate: LocalDate? = null

    @OptIn(ExperimentalTime::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _baseBinding = FragmentDialogBaseBinding.inflate(inflater, container, false)

        _binding =
            FragmentDialogAddEventBinding.inflate(
                layoutInflater,
                baseBinding.root as ViewGroup,
                false
            )

        baseBinding.fragmentDialogBaseContainer.addView(binding.root)

        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.add_event_dialog_title)

        newEvent = EventEntity()

        baseBinding.fragmentDialogBaseSaveBtn.setOnClickListener {
            newEvent?.let { event ->
                event.title = binding.fdAddEventTitleEdt.editText!!.text.toString()
                event.date = pickedDate!!
                event.timeStart = pickedStartTime!!
                event.timeEnd = pickedEndTime!!

                onPositiveButtonClicked(event)
            }
            dialog!!.dismiss()

            val snackbar = Snackbar.make(
                requireParentFragment().requireView(),
                R.string.event_saved_snack,
                Snackbar.LENGTH_SHORT
            )

            try {
                snackbar.setAnchorView(R.id.activity_main_bottom_nav_bar)
            }
            catch (_: java.lang.IllegalArgumentException) {}

            snackbar.show()
        }

        baseBinding.fragmentDialogBaseCloseBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.fdAddEventDateEdt.editText!!.setOnClickListener {
            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(
                    pickedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                        ?: MaterialDatePicker.todayInUtcMilliseconds()
                )
                .build()

            materialDatePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = materialDatePicker.selection!!

                pickedDate =
                    Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()

                binding.fdAddEventDateEdt.editText!!.setText(
                    pickedDate!!.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                )
            }
            materialDatePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }

        binding.fdAddEventTimeStartEdt.editText!!.setOnClickListener {
            showTimePicker(requireContext(),
                parentFragmentManager,
                pickedStartTime,
                { pickedStartTime = it },
                binding.fdAddEventTimeStartEdt.editText!!,
                "START_TIME_PICKER_TAG"
            )
        }

        binding.fdAddEventTimeEndEdt.editText!!.setOnClickListener {
            showTimePicker(
                requireContext(),
                parentFragmentManager,
                pickedEndTime,
                { pickedEndTime = it },
                binding.fdAddEventTimeEndEdt.editText!!,
                "END_TIME_PICKER_TAG"
            )
        }

        additionalOnCreateView()

        return baseBinding.root
    }

    protected open fun onPositiveButtonClicked(event: EventEntity) {
        viewModel.addEvent(event)
    }

    protected open fun additionalOnCreateView() {}
}

class EditEventDialogFragment : AddEventDialogFragment() {

    private val args by navArgs<EditEventDialogFragmentArgs>()

    override fun additionalOnCreateView() {
        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.edit_event_dialog_title)
    }

    override fun onPositiveButtonClicked(event: EventEntity) {
//        viewModel.updateEvent(event)
    }
}