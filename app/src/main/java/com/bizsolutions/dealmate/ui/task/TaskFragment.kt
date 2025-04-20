package com.bizsolutions.dealmate.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentTaskBinding
import com.bizsolutions.dealmate.ui.ToolbarMenuHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(), ToolbarMenuHandler {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onToolbarMenuItemClicked(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                Toast.makeText(requireContext(), "Edit task", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}