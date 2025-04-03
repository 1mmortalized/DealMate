package com.bizsolutions.dealmate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ClientRepository) : ViewModel() {

    val clients = repository.allClients.asLiveData()

    fun addClient(name: String, email: String) {
        viewModelScope.launch {
            repository.insert(ClientEntity(name = name, email = email))
        }
    }

    fun removeClient(client: ClientEntity) {
        viewModelScope.launch {
            repository.delete(client)
        }
    }
}