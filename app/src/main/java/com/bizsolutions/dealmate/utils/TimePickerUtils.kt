package com.bizsolutions.dealmate.utils

import android.content.Context
import android.text.format.DateFormat
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime
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
