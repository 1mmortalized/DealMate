package com.bizsolutions.dealmate.ext

import android.content.Context
import android.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.color.MaterialColors

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

fun Context.getThemeColor(attrResId: Int, fallback: Int = Color.BLACK): Int {
    return MaterialColors.getColor(this, attrResId, fallback)
}