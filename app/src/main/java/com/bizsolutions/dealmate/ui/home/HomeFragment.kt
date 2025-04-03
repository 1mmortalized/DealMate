package com.bizsolutions.dealmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
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
import java.time.format.DateTimeFormatter

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

        val calendarAdapter = CalendarAdapter(requireContext(), calendarViewModel) { date ->
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

        setMonthText(calendarViewModel.baseIndex)
        binding.todayBtn.isInvisible = true

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                recyclerView.smoothScrollToPosition(position)
                calendarAdapter.selectItem(position)
                setMonthText(position)

                binding.todayBtn.isInvisible = calendarViewModel.baseIndex == position
            }
        })

        binding.todayBtn.setOnClickListener {
            viewPager.setCurrentItem(calendarViewModel.baseIndex, true)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMonthText(listPosition: Int) {
        binding.monthTxt.text =
            calendarViewModel.getDateForPosition(listPosition)
                .format(DateTimeFormatter.ofPattern("LLLL yyyy"))
                .replaceFirstChar { it.uppercase() }
    }
}