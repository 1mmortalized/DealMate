package com.bizsolutions.dealmate.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.DealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    dealRepository: DealRepository
) : ViewModel() {
    val allDeals = dealRepository.allDeals.asLiveData()
}