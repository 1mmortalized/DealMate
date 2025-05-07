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
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.ext.observeOnce
import com.bizsolutions.dealmate.ui.FullscreenDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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
                client.name = binding.fdAddClientNameEdt.editText!!.text.toString()
                client.phone = binding.fdAddClientPhoneEdt.editText!!.text.toString()
                client.email = binding.fdAddClientEmailEdt.editText!!.text.toString()

                onPositiveButtonClicked(client)
            }
            dialog!!.dismiss()

            val snackbar = Snackbar.make(
                requireParentFragment().requireView(),
                R.string.client_saved_snack,
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

        val clientId = args.clientId
        viewModel.getClient(clientId).observeOnce(viewLifecycleOwner) { client ->
            newClient?.id = client.id

            binding.fdAddClientNameEdt.editText!!.setText(client.name)
            binding.fdAddClientPhoneEdt.editText!!.setText(client.phone)
            binding.fdAddClientEmailEdt.editText!!.setText(client.email)
        }
    }

    override fun onPositiveButtonClicked(client: ClientEntity) {
        viewModel.updateClient(client)
    }
}