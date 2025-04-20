package com.bizsolutions.dealmate.ext

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.safeNavigate(navDirections: NavDirections, extras: FragmentNavigator.Extras) {
    try {
        navigate(navDirections, extras)
    } catch (e: IllegalArgumentException) {
        Log.e("DealMate", e.toString())
    }
}

fun NavController.safeNavigate(navDirections: NavDirections) {
    try {
        navigate(navDirections)
    } catch (e: IllegalArgumentException) {
        Log.e("DealMate", e.toString())
    }
}