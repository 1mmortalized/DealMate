package com.bizsolutions.dealmate.ui.home.calendar

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DayPagerAdapter(fragment: Fragment, private val viewModel: CalendarViewModel) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun getItemId(position: Int): Long {
        return viewModel.getDateForPosition(position).toEpochDay()
    }

    override fun containsItem(itemId: Long): Boolean {
        return true
    }

    override fun createFragment(position: Int): Fragment {
        val date = viewModel.getDateForPosition(position)
        return DayFragment.newInstance(date)
    }
}
