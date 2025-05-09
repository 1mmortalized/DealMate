package com.bizsolutions.dealmate.ui.keywords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentKeywordsBinding
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordsFragment : Fragment() {
    private var _binding: FragmentKeywordsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: KeywordsRecViewAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: KeywordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val anim = MaterialFadeThrough().apply {
            addTarget(R.id.fragment_keywords)
        }

        enterTransition = anim
        exitTransition = anim
        reenterTransition = anim
        returnTransition = anim
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeywordsBinding.inflate(inflater, container, false)

        _adapter = KeywordsRecViewAdapter()
        binding.fragmentKeywordsRecView.adapter = adapter
        binding.fragmentKeywordsRecView.setHasFixedSize(true)

        viewModel.allKeywords.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}