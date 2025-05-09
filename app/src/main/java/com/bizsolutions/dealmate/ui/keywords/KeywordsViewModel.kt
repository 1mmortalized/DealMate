package com.bizsolutions.dealmate.ui.keywords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bizsolutions.dealmate.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KeywordsViewModel @Inject constructor(
    keywordRepository: KeywordRepository
) : ViewModel() {
    val allKeywords = keywordRepository.allKeywords.asLiveData()
}