package com.bizsolutions.dealmate.ui.home.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bizsolutions.dealmate.databinding.FragmentDayBinding
import java.time.LocalDate

class DayFragment : Fragment() {
    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)

        val epochDay = arguments?.getLong("epochDay") ?: 0L
        val date = LocalDate.ofEpochDay(epochDay)

        return binding.root
    }

    companion object {
        fun newInstance(date: LocalDate) = DayFragment().apply {
            arguments = Bundle().apply {
                putLong("epochDay", date.toEpochDay())
            }
        }
    }
}