package com.bizsolutions.dealmate.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bizsolutions.dealmate.repository.DealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    dealRepository: DealRepository
) : ViewModel() {
    val allDeals = dealRepository.allDeals.asLiveData()
}