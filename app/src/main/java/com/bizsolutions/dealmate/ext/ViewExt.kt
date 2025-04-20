package com.bizsolutions.dealmate.ext

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible

fun View.fadeIn(duration: Long = 200) {
    alpha = 0f
    isVisible = true
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long = 200, onEnd: () -> Unit = {}) {
    animate().alpha(0f).setDuration(duration).withEndAction {
        isGone = true
        onEnd()
    }.start()
}

fun View.switchFadeTo(other: View, duration: Long = 100) {
    this.fadeOut(duration) {
        other.fadeIn(duration)
    }
}