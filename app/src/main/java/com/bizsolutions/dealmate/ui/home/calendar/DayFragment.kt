package com.bizsolutions.dealmate.ui.home.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDayBinding
import com.bizsolutions.dealmate.ui.home.CallRecViewAdapter
import com.bizsolutions.dealmate.ui.home.DayViewModel
import com.bizsolutions.dealmate.ui.home.EventRecViewAdapter
import com.bizsolutions.dealmate.ui.home.TaskRecViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class DayFragment : Fragment() {
    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private var _eventAdapter: EventRecViewAdapter? = null
    private val eventAdapter get() = _eventAdapter!!

    private var _taskAdapter: TaskRecViewAdapter? = null
    private val taskAdapter get() = _taskAdapter!!

    private var _callAdapter: CallRecViewAdapter? = null
    private val callAdapter get() = _callAdapter!!

    private val viewModel: DayViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)

        val epochDay = arguments?.getLong("epochDay") ?: 0L
        val date = LocalDate.ofEpochDay(epochDay)

        setupEvents(date)
        setupTasks(date)
        setupCalls(date)

        return binding.root
    }

    private fun <T> setupRecyclerView(
        recyclerView: RecyclerView,
        itemLayout: Int,
        adapter: ListAdapter<T, *>,
        getItems: (LocalDate) -> LiveData<List<T>>,
        date: LocalDate
    ) {
        recyclerView.adapter = adapter

        val itemView = layoutInflater.inflate(itemLayout, binding.root, false)

        // Returns wrong width but the height is alright
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        itemView.measure(widthSpec, heightSpec)

        val totalHeight = itemView.measuredHeight * 3 + itemView.marginBottom * 2
        recyclerView.layoutParams.height = totalHeight
        recyclerView.requestLayout()

        getItems(date).observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        @SuppressLint("ClickableViewAccessibility")
        recyclerView.setOnTouchListener { v, event ->
            if (recyclerView.canScrollVertically(1) || recyclerView.canScrollVertically(-1)) {
                v.parent?.parent?.requestDisallowInterceptTouchEvent(true)
            }
            false
        }
    }

    private fun setupEvents(date: LocalDate) {
        _eventAdapter = EventRecViewAdapter({}, {})
        setupRecyclerView(
            binding.fragmentDayEventsRecView,
            R.layout.item_event,
            eventAdapter,
            { viewModel.getEventsByDate(date) },
            date
        )
    }

    private fun setupTasks(date: LocalDate) {
        _taskAdapter = TaskRecViewAdapter({}, {})
        setupRecyclerView(
            binding.fragmentDayTasksRecView,
            R.layout.item_task,
            taskAdapter,
            { viewModel.getTasksByDate(date) },
            date
        )
    }

    private fun setupCalls(date: LocalDate) {
        _callAdapter = CallRecViewAdapter({}, {})
        setupRecyclerView(
            binding.fragmentDayCallsRecView,
            R.layout.item_call,
            callAdapter,
            { viewModel.getCallsByDate(date) },
            date
        )
    }

    companion object {
        fun newInstance(date: LocalDate) = DayFragment().apply {
            arguments = Bundle().apply {
                putLong("epochDay", date.toEpochDay())
            }
        }
    }
}