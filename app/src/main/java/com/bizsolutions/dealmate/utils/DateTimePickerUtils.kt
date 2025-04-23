package com.bizsolutions.dealmate.utils

import android.content.Context
import android.text.format.DateFormat
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun showTimePicker(
    context: Context,
    fragmentManager: FragmentManager,
    initialTime: LocalTime?,
    onTimePicked: (LocalTime) -> Unit,
    targetEditText: EditText,
    tag: String,
) {
    val is24HourFormat = DateFormat.is24HourFormat(context)

    val builder = MaterialTimePicker.Builder()
        .setTimeFormat(
            if (is24HourFormat)
                TimeFormat.CLOCK_24H
            else
                TimeFormat.CLOCK_12H
        )
        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)

    initialTime?.let {
        builder.setHour(it.hour)
        builder.setMinute(it.minute)
    }

    val picker = builder.build()
    picker.addOnPositiveButtonClickListener {
        val pickedTime = LocalTime.of(picker.hour, picker.minute)
        onTimePicked(pickedTime)
        targetEditText.setText(
            pickedTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        )
    }

    picker.show(fragmentManager, tag)
}

fun showDatePicker(
    initialDate: LocalDate? = null,
    fragmentManager: FragmentManager,
    tag: String = "DATE_PICKER",
    onDatePicked: (LocalDate) -> Unit
) {
    val selectionMillis = initialDate
        ?.atStartOfDay(ZoneId.systemDefault())
        ?.toInstant()
        ?.toEpochMilli()
        ?: MaterialDatePicker.todayInUtcMilliseconds()

    val materialDatePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(selectionMillis)
        .build()

    materialDatePicker.addOnPositiveButtonClickListener { selection ->
        val pickedDate = Instant.ofEpochMilli(selection)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        onDatePicked(pickedDate)
    }

    materialDatePicker.show(fragmentManager, tag)
}
