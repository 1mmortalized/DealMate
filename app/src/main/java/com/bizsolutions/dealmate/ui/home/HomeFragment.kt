package com.bizsolutions.dealmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bizsolutions.dealmate.databinding.FragmentHomeBinding
import com.bizsolutions.dealmate.ui.home.calendar.CalendarAdapter
import com.bizsolutions.dealmate.ui.home.calendar.CalendarViewModel
import com.bizsolutions.dealmate.ui.home.calendar.CenterLinearLayoutManager
import com.bizsolutions.dealmate.ui.home.calendar.DayPagerAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        val recyclerView = binding.recyclerView
        val viewPager = binding.viewPager

        val calendarAdapter = CalendarAdapter(calendarViewModel) { date ->
            val newPosition = calendarViewModel.getPageForDate(date)
            viewPager.setCurrentItem(newPosition, true)
        }

        recyclerView.apply {
            layoutManager = CenterLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter

            scrollToPosition(calendarViewModel.baseIndex)
            smoothScrollToPosition(calendarViewModel.baseIndex)
        }

        viewPager.adapter = DayPagerAdapter(requireActivity(), calendarViewModel)
        viewPager.setCurrentItem(calendarViewModel.baseIndex, false)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                recyclerView.smoothScrollToPosition(position)
                calendarAdapter.selectItem(position)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}