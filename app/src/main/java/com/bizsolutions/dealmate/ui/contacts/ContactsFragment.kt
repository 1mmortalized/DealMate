package com.bizsolutions.dealmate.ui.contacts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bizsolutions.dealmate.databinding.FragmentContactsBinding
import com.google.android.material.color.MaterialColors
import com.wynneplaga.materialScrollBar2.inidicators.AlphabeticIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ContactRecViewAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        _adapter = ContactRecViewAdapter(
            requireContext(), {}, {}
        )
        binding.fragmentContactsRecView.adapter = adapter
        binding.fragmentContactsRecView.setHasFixedSize(true)
        setupScrollbar()

        viewModel.allClients.observe(viewLifecycleOwner) { list ->
            adapter.groupSubmitList(list)
        }

        return binding.root
    }

    private fun setupScrollbar() {
        val colorSecondaryContainer: Int = MaterialColors.getColor(
            requireContext(),
            com.google.android.material.R.attr.colorSecondaryContainer,
            Color.BLACK
        )

        val colorOnSecondaryContainer: Int = MaterialColors.getColor(
            requireContext(),
            com.google.android.material.R.attr.colorOnSecondaryContainer,
            Color.BLACK
        )

        binding.fragmentContactsScrollbar.indicator = AlphabeticIndicator(
            requireContext(),
            adapter,
            colorSecondaryContainer,
            colorOnSecondaryContainer
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}