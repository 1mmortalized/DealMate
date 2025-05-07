package com.bizsolutions.dealmate.ext

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bizsolutions.dealmate.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Context.getThemeColor(attrResId: Int, fallback: Int = Color.BLACK): Int {
    return MaterialColors.getColor(this, attrResId, fallback)
}

fun Context.showDeleteConfirmationDialog(
    @StringRes titleRes: Int,
    @StringRes snackbarTextRes: Int,
    snackbarParent: View,
    onConfirm: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(getString(titleRes))
        .setMessage(R.string.del_dlg_msg)
        .setPositiveButton(getString(R.string.dlg_pos_answer)) { _, _ ->
            onConfirm()

            Snackbar.make(snackbarParent, snackbarTextRes, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.activity_main_bottom_nav_bar)
                .show()
        }
        .setNegativeButton(getString(R.string.dlg_neg_answer), null)
        .show()
}
