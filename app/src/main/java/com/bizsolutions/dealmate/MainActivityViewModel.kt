package com.bizsolutions.dealmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
import com.chaquo.python.Python
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
        val py = Python.getInstance()
        val keywordsModule = py.getModule("keywords")
        val extractKeywordsFunction = keywordsModule["extract_keywords"]

        viewModelScope.launch {
            val today = LocalDate.now()
            val uncompletedTasks = taskRepository.getOverdueUncompletedTasks(today)

            uncompletedTasks.forEach { task ->
                val updatedTask = task.copy(postponed = true)
                taskRepository.update(updatedTask)

                val keywordsRaw = extractKeywordsFunction?.call(task.title)
                val keywords = keywordsRaw?.asList()?.map { it.toJava(String::class.java) }

                keywords?.forEach { word ->
                    keywordRepository.increasePostponedCount(word)
                }
            }
        }
    }
}