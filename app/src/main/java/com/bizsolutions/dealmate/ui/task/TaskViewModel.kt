package com.bizsolutions.dealmate.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import com.chaquo.python.Python
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val keywordRepository: KeywordRepository
) : ViewModel() {
    fun getTask(id: Int) = taskRepository.getTask(id).asLiveData()

    fun updateProgress(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateProgress(task.id, task.progress)

            if (task.progress == 100 && !task.postponed) {
                val py = Python.getInstance()
                val keywordsModule = py.getModule("keywords")
                val extractKeywordsFunction = keywordsModule["extract_keywords"]

                val updatedTask = task.copy(postponed = true)
                taskRepository.update(updatedTask)

                val keywordsRaw = extractKeywordsFunction?.call(task.title)
                val keywords = keywordsRaw?.asList()?.map { it.toJava(String::class.java) }

                keywords?.forEach { word ->
                    keywordRepository.increaseCompletedCount(word)
                }
            }
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            taskRepository.delete(id)
        }
    }
}