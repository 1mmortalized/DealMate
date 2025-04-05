package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.TaskDao
import com.bizsolutions.dealmate.db.TaskEntity
import com.bizsolutions.dealmate.db.TaskWithClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskRepository(private val dao: TaskDao) {
    fun allTasksByDate(date: LocalDate): Flow<List<TaskWithClient>> = dao.getAllTasksByDate(date)

    suspend fun insert(event: TaskEntity) {
        dao.insertTask(event)
    }

    suspend fun delete(event: TaskEntity) {
        dao.deleteTask(event)
    }
}