package com.bizsolutions.dealmate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.CallEntity
import com.bizsolutions.dealmate.db.EventEntity
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.repository.CallRepository
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.EventRepository
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import com.bizsolutions.dealmate.ui.task.ScoredKeyword
import com.bizsolutions.dealmate.ui.task.TaskPriority
import com.bizsolutions.dealmate.utils.extractKeywords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val eventRepository: EventRepository,
    val taskRepository: TaskRepository,
    val callRepository: CallRepository,
    val keywordRepository: KeywordRepository,
    clientRepository: ClientRepository
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

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.update(event)
        }
    }

    fun removeEvent(event: EventEntity) {
        viewModelScope.launch {
            eventRepository.delete(event)
        }
    }

    fun completeEvent(id: Int, completed: Boolean = true) {
        viewModelScope.launch {
            eventRepository.completeEvent(id, completed)
        }
    }

    fun completeCall(id: Int, completed: Boolean = true) {
        viewModelScope.launch {
            callRepository.completeCall(id, completed)
        }
    }

    fun getTask(id: Int) = taskRepository.getTask(id).asLiveData()

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    fun updateTaskProgress(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateProgress(task.id, task.progress)

            if (task.progress == 100 && !task.postponed) {
                val updatedTask = task.copy(postponed = true)
                taskRepository.update(updatedTask)

                val keywords = extractKeywords(task.title)
                keywords?.forEach { word ->
                    keywordRepository.increaseCompletedCount(word)
                }
            }
        }
    }

    fun getCall(id: Int) = callRepository.getCall(id).asLiveData()

    fun addCall(call: CallEntity) {
        viewModelScope.launch {
            callRepository.insert(call)
        }
    }

    fun updateCall(call: CallEntity) {
        viewModelScope.launch {
            callRepository.update(call)
        }
    }

    fun removeCall(call: CallEntity) {
        viewModelScope.launch {
            callRepository.delete(call)
        }
    }

    val allClients = clientRepository.allClients.asLiveData()

    suspend fun suggestTaskPriority(
        taskText: String,
    ): TaskPriority {
        val extractedKeywords = extractKeywords(taskText)
        if (extractedKeywords.isNullOrEmpty()) return TaskPriority.LOW

        val keywordEntities = keywordRepository.getKeywords(extractedKeywords)

        val scores = keywordEntities.mapNotNull { keyword ->
            val total = keyword.completedCount + keyword.postponedCount
            if (total == 0) return@mapNotNull null

            val urgencyRatio = keyword.postponedCount.toDouble() / total
            val recencyBonus = if (keyword.lastSeen != null &&
                keyword.lastSeen >= LocalDate.now().minusDays(3)
            ) 0.1 else 0.0

            ScoredKeyword(
                word = keyword.word,
                priorityScore = urgencyRatio + recencyBonus
            )
        }

        if (scores.isEmpty()) return TaskPriority.LOW

        val averageScore = scores.map { it.priorityScore }.average()

        return when {
            averageScore >= 0.7 -> TaskPriority.HIGH
            averageScore >= 0.4 -> TaskPriority.MEDIUM
            else -> TaskPriority.LOW
        }
    }
}