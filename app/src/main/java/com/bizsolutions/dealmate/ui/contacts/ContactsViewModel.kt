package com.bizsolutions.dealmate.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    val clientRepository: ClientRepository
) : ViewModel() {
    val allClients = clientRepository.allClients.asLiveData()

    fun addClient(client: ClientEntity) {
        viewModelScope.launch {
            clientRepository.insert(client)
        }
    }
}