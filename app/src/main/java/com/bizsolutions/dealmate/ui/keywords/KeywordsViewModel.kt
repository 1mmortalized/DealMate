package com.bizsolutions.dealmate.ui.keywords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeywordsViewModel @Inject constructor(
    keywordRepository: KeywordRepository
) : ViewModel() {
    val allKeywords = keywordRepository.allKeywords.asLiveData()
}