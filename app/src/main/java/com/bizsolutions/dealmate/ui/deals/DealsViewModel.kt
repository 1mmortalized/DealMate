package com.bizsolutions.dealmate.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.DealEntity
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.DealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    val dealRepository: DealRepository,
    clientRepository: ClientRepository
) : ViewModel() {
    val allDeals = dealRepository.allDeals.asLiveData()
    val allClients = clientRepository.allClients.asLiveData()

    fun getDeal(id: Int) = dealRepository.getDeal(id).asLiveData()

    fun addDeal(deal: DealEntity) {
        viewModelScope.launch {
            dealRepository.insert(deal)
        }
    }

    fun updateDeal(deal: DealEntity) {
        viewModelScope.launch {
            dealRepository.update(deal)
        }
    }

    fun removeDeal(deal: DealEntity) {
        viewModelScope.launch {
            dealRepository.delete(deal)
        }
    }
}