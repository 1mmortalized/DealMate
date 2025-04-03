package com.bizsolutions.dealmate.ui.home.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DayPagerAdapter(activity: FragmentActivity, private val viewModel: CalendarViewModel) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val date = viewModel.getDateForPosition(position)
        return DayFragment.newInstance(date)
    }
}
