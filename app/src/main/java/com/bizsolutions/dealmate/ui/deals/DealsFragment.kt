package com.bizsolutions.dealmate.ui.deals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.data.currency.CurrencyManager
import com.bizsolutions.dealmate.databinding.FragmentDealsBinding
import com.bizsolutions.dealmate.db.DealWithClient
import com.bizsolutions.dealmate.ext.safeNavigate
import com.bizsolutions.dealmate.ui.MyTransitionListener
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.math.roundToInt

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
            requireContext(),
            { dealId ->
                val directions = DealsFragmentDirections.actionDealToEditDeal(dealId)
                findNavController().safeNavigate(directions)
            },
            {}
        )
        binding.fragmentDealsRecView.adapter = adapter

        viewModel.allDeals.observe(viewLifecycleOwner) { list ->
            adapter.groupSubmitList(list)

            val decimalFormat = DecimalFormat("#,###")
            val totalIncome = calculateIncome(list)

            if (totalIncome != null) {
                val formattedTotalIncome = decimalFormat.format(totalIncome.roundToInt())
                @SuppressLint("SetTextI18n")
                binding.fragmentDealsTotalIncomeValueTxt.text =
                    "%s USD".format(formattedTotalIncome)
            }
            else {
                @SuppressLint("SetTextI18n")
                binding.fragmentDealsTotalIncomeValueTxt.text = "NaN"
            }

            val now = LocalDate.now()
            val dealsThisMonth = list.filter {
                it.deal.date.month == now.month && it.deal.date.year == now.year
            }
            val monthlyIncome = calculateIncome(dealsThisMonth)

            if (monthlyIncome != null) {
                val formattedMonthlyIncome = decimalFormat.format(monthlyIncome.roundToInt())
                @SuppressLint("SetTextI18n")
                binding.fragmentDealsMonthlyIncomeTxt.text =
                    "%s USD".format(formattedMonthlyIncome)
            }
            else {
                @SuppressLint("SetTextI18n")
                binding.fragmentDealsMonthlyIncomeTxt.text = "NaN"
            }
        }

        binding.fragmentDealsFab.setOnClickListener {
            val directions = DealsFragmentDirections.actionDealToAddDeal()
            findNavController().safeNavigate(directions)
        }

        return binding.root
    }

    private fun calculateIncome(list: List<DealWithClient>): Float? {
        var income = 0f

        list.forEach {
            val currencyCode = it.deal.currency.uppercase()
            val currencyRate = CurrencyManager.currencyRates?.get(currencyCode)?.toFloat()
            if (currencyRate == null) return null

            val amount = it.deal.amount
            income += amount / currencyRate
        }

        return income
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}