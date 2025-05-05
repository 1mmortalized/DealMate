package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY priority ASC, id DESC")
    fun getAllTasksByDate(date: LocalDate): Flow<List<TaskWithClient>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<TaskWithClient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: Int, progress: Int)
}