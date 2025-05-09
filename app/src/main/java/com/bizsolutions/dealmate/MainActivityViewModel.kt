package com.bizsolutions.dealmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import com.bizsolutions.dealmate.utils.extractKeywords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val taskRepository: TaskRepository,
    val keywordRepository: KeywordRepository
) : ViewModel() {

    fun updateKeywords() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val uncompletedTasks = taskRepository.getOverdueUncompletedTasks(today)

            uncompletedTasks.forEach { task ->
                val updatedTask = task.copy(postponed = true)
                taskRepository.update(updatedTask)

                val keywords = extractKeywords(task.title)
                keywords?.forEach { word ->
                    keywordRepository.increasePostponedCount(word)
                }
            }
        }
    }
}