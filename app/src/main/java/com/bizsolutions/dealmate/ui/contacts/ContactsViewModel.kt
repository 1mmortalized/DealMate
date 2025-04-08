package com.bizsolutions.dealmate.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bizsolutions.dealmate.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    clientRepository: ClientRepository
) : ViewModel() {
    val allClients = clientRepository.allClients.asLiveData()
}