package com.bizsolutions.dealmate.ui.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.Transition
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDealsBinding
import com.bizsolutions.dealmate.ui.MyTransitionListener
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: DealRecViewAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: DealsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            addTarget(R.id.fragment_contacts)

            addListener(object : MyTransitionListener() {
                override fun onTransitionStart(transition: Transition) {
                    binding.fragmentDealsFab.show()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealsBinding.inflate(inflater, container, false)

        binding.fragmentDealsFab.doOnLayout {
            binding.fragmentDealsRecView.updatePadding(
                bottom = binding.fragmentDealsFab.measuredHeight
                        + binding.fragmentDealsFab.marginBottom
            )
        }

        _adapter = DealRecViewAdapter(
            {}, {}
        )
        binding.fragmentDealsRecView.adapter = adapter

        viewModel.allDeals.observe(viewLifecycleOwner) { list ->
            adapter.groupSubmitList(list)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}