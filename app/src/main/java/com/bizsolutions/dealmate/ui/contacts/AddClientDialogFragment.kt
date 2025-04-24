package com.bizsolutions.dealmate.ui.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bizsolutions.dealmate.R
import com.bizsolutions.dealmate.databinding.FragmentDialogAddClientBinding
import com.bizsolutions.dealmate.databinding.FragmentDialogBaseBinding
import com.bizsolutions.dealmate.db.CallEntity
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.bizsolutions.dealmate.ui.home.AddTaskDialogFragment
import com.bizsolutions.dealmate.ui.home.EditCallDialogFragmentArgs
import com.bizsolutions.dealmate.utils.showDatePicker
import com.bizsolutions.dealmate.utils.showTimePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
open class AddClientDialogFragment : FullscreenDialogFragment() {

    private var _binding: FragmentDialogAddClientBinding? = null
    protected val binding get() = _binding!!

    protected var newClient: ClientEntity? = null

    protected val viewModel: ContactsViewModel by viewModels()

    @OptIn(ExperimentalTime::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _baseBinding = FragmentDialogBaseBinding.inflate(inflater, container, false)

        _binding =
            FragmentDialogAddClientBinding.inflate(
                layoutInflater,
                baseBinding.root as ViewGroup,
                false
            )

        baseBinding.fragmentDialogBaseContainer.addView(binding.root)

        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.add_client_dialog_title)

        newClient = ClientEntity()

        baseBinding.fragmentDialogBaseSaveBtn.setOnClickListener {
            newClient?.let { client ->
                client.title = binding.fdAddCallTitleEdt.editText!!.text.toString()
                client.date = pickedDate!!
                client.time = pickedTime!!

                client.clientId = clients.find {
                    it.name == binding.fdAddCallClientEdtTxt.text.toString()
                }?.id ?: 0

                onPositiveButtonClicked(client)
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

    protected open fun onPositiveButtonClicked(client: ClientEntity) {
        viewModel.addClient(client)
    }

    protected open fun additionalOnCreateView() {}
}

class EditClientDialogFragment : AddClientDialogFragment() {

    private val args by navArgs<EditClientDialogFragmentArgs>()

    override fun additionalOnCreateView() {
        baseBinding.fragmentDialogBaseTitleTxt.setText(R.string.edit_client_dialog_title)
    }

    override fun onPositiveButtonClicked(client: ClientEntity) {
//        viewModel.updateClient(client)
    }
}