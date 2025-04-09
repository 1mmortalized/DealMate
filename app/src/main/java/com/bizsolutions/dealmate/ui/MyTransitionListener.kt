package com.bizsolutions.dealmate.ui

import androidx.transition.Transition

open class MyTransitionListener : Transition.TransitionListener {
    override fun onTransitionStart(transition: Transition) {}
    override fun onTransitionEnd(transition: Transition) {}
    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionResume(transition: Transition) {}
}