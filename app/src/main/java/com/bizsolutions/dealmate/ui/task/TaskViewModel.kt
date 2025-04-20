package com.bizsolutions.dealmate.ui.task

import androidx.lifecycle.ViewModel
import com.bizsolutions.dealmate.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    val taskRepository: TaskRepository
) : ViewModel() {
//    fun getEvent() = eventRepository. .asLiveData()
}