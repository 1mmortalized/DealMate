package com.bizsolutions.dealmate.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDialogAddCallBinding
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.bizsolutions.dealmate.db.CallEntity
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.bizsolutions.dealmate.utils.showDatePicker
import com.bizsolutions.dealmate.utils.showTimePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
open class AddCallDialogFragment : FullscreenDialogFragment() {

    private var _binding: FragmentDialogAddCallBinding? = null
    protected val binding get() = _binding!!

    protected var newCall: CallEntity? = null

    protected val viewModel: HomeViewModel by viewModels()

    protected var pickedDate: LocalDate? = null
    protected var pickedTime: LocalTime? = null

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
            FragmentDialogAddCallBinding.inflate(
                layoutInflater,
                baseBinding.root as ViewGroup,
                false
            )

        baseBinding.fragmentDialogBaseContainer.addView(binding.root)

        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.add_call_dialog_title)

        newCall = CallEntity()

        baseBinding.fragmentDialogBaseSaveBtn.setOnClickListener {
            newCall?.let { call ->
                call.title = binding.fdAddCallTitleEdt.editText!!.text.toString()
                call.date = pickedDate!!
                call.time = pickedTime!!

                call.clientId = clients.find {
                    it.name == binding.fdAddCallClientEdtTxt.text.toString()
                }?.id ?: 0

                onPositiveButtonClicked(call)
            }
            dialog!!.dismiss()

            val snackbar = Snackbar.make(
                requireParentFragment().requireView(),
                R.string.call_saved_snack,
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

        binding.fdAddCallDateEdt.editText!!.setOnClickListener {
            showDatePicker(
                pickedDate,
                parentFragmentManager,
                "DATE_PICKER_TAG"
            ) { date ->
                pickedDate = date
                binding.fdAddCallDateEdt.editText!!.setText(
                    date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                )
            }
        }

        binding.fdAddCallTimeEdt.editText!!.setOnClickListener {
            showTimePicker(requireContext(),
                parentFragmentManager,
                pickedTime,
                { pickedTime = it },
                binding.fdAddCallTimeEdt.editText!!,
                "START_TIME_PICKER_TAG"
            )
        }

        viewModel.allClients.observeOnce(viewLifecycleOwner) { clients ->
            (binding.fdAddCallClientEdtTxt as MaterialAutoCompleteTextView)
                .setSimpleItems(clients.map { it.name }.toTypedArray())
            this.clients = clients
        }

        additionalOnCreateView()

        return baseBinding.root
    }

    protected open fun onPositiveButtonClicked(call: CallEntity) {
        viewModel.addCall(call)
    }

    protected open fun additionalOnCreateView() {}
}

class EditCallDialogFragment : AddCallDialogFragment() {

    private val args by navArgs<EditCallDialogFragmentArgs>()

    override fun additionalOnCreateView() {
        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.edit_call_dialog_title)
    }

    override fun onPositiveButtonClicked(call: CallEntity) {
//        viewModel.updateCall(call)
    }
}