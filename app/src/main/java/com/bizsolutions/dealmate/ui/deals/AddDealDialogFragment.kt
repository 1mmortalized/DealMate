package com.bizsolutions.dealmate.ui.deals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.data.currency.CurrencyManager
import com.bizsolutions.dealmate.databinding.FragmentDialogAddDealBinding
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.DealEntity
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.bizsolutions.dealmate.utils.showDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
open class AddDealDialogFragment : FullscreenDialogFragment() {

    private var _binding: FragmentDialogAddDealBinding? = null
    protected val binding get() = _binding!!

    protected var newDeal: DealEntity? = null

    protected val viewModel: DealsViewModel by viewModels()

    protected var pickedDate: LocalDate? = null

    private lateinit var clients: List<ClientEntity>

    @OptIn(ExperimentalTime::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _baseBinding = FragmentDialogBaseBinding.inflate(inflater, container, false)

        _binding =
            FragmentDialogAddDealBinding.inflate(
                layoutInflater,
                baseBinding.root as ViewGroup,
                false
            )

        baseBinding.fragmentDialogBaseContainer.addView(binding.root)

        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.add_deal_dialog_title)

        newDeal = DealEntity()

        baseBinding.fragmentDialogBaseSaveBtn.setOnClickListener {
            newDeal?.let { deal ->
                deal.title = binding.fdAddDealTitleEdt.editText!!.text.toString()
                deal.amount = binding.fdAddDealAmountEdt.editText!!.text.toString().toInt()
                deal.currency = binding.fdAddDealCurrencyEdt.text.toString()
                deal.date = pickedDate!!

                deal.clientId = clients.find {
                    it.name == binding.fdAddDealClientEdtTxt.text.toString()
                }?.id ?: 0

                onPositiveButtonClicked(deal)
            }
            dialog!!.dismiss()

            val snackbar = Snackbar.make(
                requireParentFragment().requireView(),
                R.string.deal_saved_snack,
                Snackbar.LENGTH_SHORT
            )

            try {
                snackbar.setAnchorView(R.id.activity_main_bottom_nav_bar)
            }
            catch (_: IllegalArgumentException) {}

            snackbar.show()
        }

        baseBinding.fragmentDialogBaseCloseBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        val currencies = CurrencyManager.currencyRates?.map { it.key }
            ?.filter { it != "RUB" }?.toTypedArray()
        currencies?.let {
            (binding.fdAddDealCurrencyEdt as MaterialAutoCompleteTextView)
                .setSimpleItems(it)
        }

        binding.fdAddDealDateEdt.editText!!.setOnClickListener {
            showDatePicker(
                pickedDate,
                parentFragmentManager,
                "DATE_PICKER_TAG"
            ) { date ->
                pickedDate = date
                binding.fdAddDealDateEdt.editText!!.setText(
                    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                )
            }
        }

        viewModel.allClients.observeOnce(viewLifecycleOwner) { clients ->
            (binding.fdAddDealClientEdtTxt as MaterialAutoCompleteTextView)
                .setSimpleItems(clients.map { it.name }.toTypedArray())
            this.clients = clients
        }

        additionalOnCreateView()

        return baseBinding.root
    }

    protected open fun onPositiveButtonClicked(deal: DealEntity) {
        viewModel.addDeal(deal)
    }

    protected open fun additionalOnCreateView() {}
}

class EditDealDialogFragment : AddDealDialogFragment() {

    private val args by navArgs<EditDealDialogFragmentArgs>()

    override fun additionalOnCreateView() {
        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.edit_deal_dialog_title)
    }

    override fun onPositiveButtonClicked(deal: DealEntity) {
//        viewModel.updateDeal(deal)
    }
}