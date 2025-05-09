package com.bizsolutions.dealmate.ui.task

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.bizsolutions.dealmate.R

enum class TaskPriority(
    val value: Int,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    val containerColor: Int,
    val onContainerColor: Int
) {
    HIGH(
        1,
        R.string.priority_high,
        R.drawable.ic_stat_3,
        com.google.android.material.R.attr.colorPrimaryContainer,
        com.google.android.material.R.attr.colorOnPrimaryContainer
    ),
    MEDIUM(
        2,
        R.string.priority_medium,
        R.drawable.ic_stat_2,
        com.google.android.material.R.attr.colorSecondaryContainer,
        com.google.android.material.R.attr.colorOnSecondaryContainer
    ),
    LOW(
        3,
        R.string.priority_low,
        R.drawable.ic_stat_1,
        com.google.android.material.R.attr.colorTertiaryContainer,
        com.google.android.material.R.attr.colorOnTertiaryContainer
    ),
    AUTO(
        4,
        R.string.priority_auto,
        0,
        0,
        0
    );

    companion object {
        fun fromValue(value: Int): TaskPriority {
            return entries.find { it.value == value } ?: LOW
        }

        fun fromLabel(context: Context, label: String): TaskPriority {
            return entries.find { context.getString(it.labelResId).equals(label, ignoreCase = true) }
                ?: LOW
        }
    }
}