package com.bizsolutions.dealmate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.repository.CallRepository
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.EventRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val taskRepository: TaskRepository,
    val callRepository: CallRepository,
    val clientRepository: ClientRepository
) : ViewModel() {
    fun getEventsByDate(date: LocalDate) = eventRepository.allEventsByDate(date).asLiveData()
    fun getTasksByDate(date: LocalDate) = taskRepository.allTasksByDate(date).asLiveData()
    fun getCallsByDate(date: LocalDate) = callRepository.allCallsByDate(date).asLiveData()

    fun getEvent(id: Int) = eventRepository.getEvent(id).asLiveData()

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.insert(event)
        }
    }

    fun removeEvents(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.delete(event)
        }
    }

    fun completeEvent(id: Int, completed: Boolean = true) {
        viewModelScope.launch {
            eventRepository.completeEvent(id, completed)
        }
    }

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun updateTaskProgress(id: Int, progress: Int) {
        viewModelScope.launch {
            taskRepository.updateProgress(id, progress)
        }
    }

    val allClients = clientRepository.allClients.asLiveData()
}