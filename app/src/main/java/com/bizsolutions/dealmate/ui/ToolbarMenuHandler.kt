package com.bizsolutions.dealmate.ui

import android.view.MenuItem

interface ToolbarMenuHandler {
    fun onToolbarMenuItemClicked(item: MenuItem): Boolean
}