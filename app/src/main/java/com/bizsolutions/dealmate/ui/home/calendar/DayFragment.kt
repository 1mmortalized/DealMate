package com.bizsolutions.dealmate.ui.home.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDayBinding
import com.bizsolutions.dealmate.ui.home.DayViewModel
import com.bizsolutions.dealmate.ui.home.EventRecViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class DayFragment : Fragment() {
    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private var _eventAdapter: EventRecViewAdapter? = null
    private val eventAdapter get() = _eventAdapter!!

    private val viewModel: DayViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)

        val epochDay = arguments?.getLong("epochDay") ?: 0L
        val date = LocalDate.ofEpochDay(epochDay)

        _eventAdapter = EventRecViewAdapter(
            {}, {}
        )
        binding.fragmentDayEventsRecView.adapter = eventAdapter

        val eventItem = layoutInflater.inflate(R.layout.item_event, binding.root, false)

        //Returns wrong width but the height is alright
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        eventItem.measure(widthSpec, heightSpec)

        val totalHeight = eventItem.measuredHeight * 3 + eventItem.marginBottom * 2
        binding.fragmentDayEventsRecView.layoutParams.height = totalHeight
        binding.fragmentDayEventsRecView.requestLayout()

        viewModel.getEventsByDate(date).observe(viewLifecycleOwner) { list ->
            eventAdapter.submitList(list)
        }

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