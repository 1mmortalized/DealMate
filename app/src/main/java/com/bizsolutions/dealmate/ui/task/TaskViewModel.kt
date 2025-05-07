package com.bizsolutions.dealmate.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    fun getTask(id: Int) = taskRepository.getTask(id).asLiveData()

    fun updateProgress(id: Int, progress: Int) {
        viewModelScope.launch {
            taskRepository.updateProgress(id, progress)
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            taskRepository.delete(id)
        }
    }
}