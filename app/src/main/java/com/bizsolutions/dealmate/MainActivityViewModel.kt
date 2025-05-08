package com.bizsolutions.dealmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val taskRepository: TaskRepository,
    val keywordRepository: KeywordRepository
) : ViewModel() {

    suspend fun getOverdueUncompletedTasks(date: LocalDate) =
        taskRepository.getOverdueUncompletedTasks(date)
}