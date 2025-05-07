package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.TaskDao
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.db.TaskWithClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskRepository(private val dao: TaskDao) {
    fun allTasksByDate(date: LocalDate): Flow<List<TaskWithClient>> = dao.getAllTasksByDate(date)
    fun getTask(id: Int) = dao.getTaskById(id)

    suspend fun insert(task: TaskEntity) {
        dao.insertTask(task)
    }

    suspend fun update(task: TaskEntity) {
        dao.updateTask(task)
    }

    suspend fun delete(id: Int) {
        dao.deleteTask(id)
    }

    suspend fun updateProgress(id: Int, progress: Int) {
        dao.updateProgress(id, progress)
    }
}